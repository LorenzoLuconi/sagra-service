package it.loreluc.sagraservice.order;

import it.loreluc.sagraservice.config.SagraSettings;
import it.loreluc.sagraservice.error.InvalidProduct;
import it.loreluc.sagraservice.error.SagraBadRequestException;
import it.loreluc.sagraservice.error.SagraNotFoundException;
import it.loreluc.sagraservice.error.SagraQuantitaNonSufficiente;
import it.loreluc.sagraservice.jpa.Order;
import it.loreluc.sagraservice.jpa.OrderProduct;
import it.loreluc.sagraservice.jpa.Product;
import it.loreluc.sagraservice.order.resource.OrderProductRequest;
import it.loreluc.sagraservice.order.resource.OrderRequest;
import it.loreluc.sagraservice.product.ProductService;
import it.loreluc.sagraservice.security.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static it.loreluc.sagraservice.error.InvalidProduct.InvalidStatus.LOCKED;
import static it.loreluc.sagraservice.error.InvalidProduct.InvalidStatus.NOT_ENOUGH_QUANTITY;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderMapper ordineMapper;
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final SagraSettings settings;
    private final UsersRepository usersRepository;

    public Order getOrderById(Long ordineId) {
        return orderRepository.findById(ordineId).orElseThrow(() -> new SagraNotFoundException("Nessun ordine trovato con l'id: " + ordineId));
    }

    @Transactional(rollbackFor = Throwable.class)
    public Order createOrder(OrderRequest orderRequest) {

        if ( orderRequest.isTakeAway() && orderRequest.getServiceNumber() > 0 ) {
            log.warn("Tentativo di creare un ordine da asporto con indicazione dei coperti: {}", orderRequest);
            throw new SagraBadRequestException("Ordine da asporto non può avere dei coperti");
        }

        final Order order = ordineMapper.toEntity(orderRequest);

        // FIXME Utente
        order.setUser(usersRepository.findById(1L).orElseThrow(() -> new RuntimeException("User not found")));

        if ( ! order.isTakeAway() && order.getServiceNumber() > 0 ) {
            if ( settings.getServiceCost().compareTo(BigDecimal.ZERO) > 0 ) {
                order.setServiceCost(
                    settings.getServiceCost().multiply(new BigDecimal(orderRequest.getServiceNumber()))
                );
            } else {
                order.setServiceCost(BigDecimal.ZERO);
            }
        }

        // FIXME manca gestione dell'utente

        for (final OrderProductRequest orderProductRequest : orderRequest.getOrderedProducts()) {
            final Product product;
            try {
                product = productService.findById(orderProductRequest.getProduct());
            } catch ( EntityNotFoundException e) {
                throw new SagraBadRequestException("Prodotto non trovato con id: " + orderProductRequest.getProduct());
            }

            if ( product.isSellLocked() ) {
                throw new SagraQuantitaNonSufficiente(InvalidProduct.of(product.getId(), LOCKED));
            }

            if ( ! productService.updateProductQuantity(product, -orderProductRequest.getQuantity()) ) {
                throw new SagraQuantitaNonSufficiente(InvalidProduct.of(product.getId(), NOT_ENOUGH_QUANTITY));
            }

            final OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrder(order);

            orderProduct.setProduct(product);
            orderProduct.setPrice(product.getPrice());
            orderProduct.setQuantity(orderProductRequest.getQuantity());
            orderProduct.setNote(orderProduct.getNote());
            order.getOrderedProducts().add(orderProduct);
        }


        order.setTotalAmount(
            order.getOrderedProducts().stream().map(op -> op.getProduct().getPrice().multiply(new BigDecimal(op.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .add(order.getServiceCost())
        );

        return orderRepository.save(order);
    }
}
