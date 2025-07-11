package it.loreluc.sagraservice.order;

import com.querydsl.jpa.impl.JPAQuery;
import it.loreluc.sagraservice.config.SagraSettings;
import it.loreluc.sagraservice.discount.DiscountService;
import it.loreluc.sagraservice.error.*;
import it.loreluc.sagraservice.jpa.*;
import it.loreluc.sagraservice.order.resource.OrderProductRequest;
import it.loreluc.sagraservice.order.resource.OrderRequest;
import it.loreluc.sagraservice.product.ProductService;
import it.loreluc.sagraservice.security.UsersRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static it.loreluc.sagraservice.error.InvalidProduct.ProductError.LOCKED;
import static it.loreluc.sagraservice.error.InvalidProduct.ProductError.NOT_ENOUGH_QUANTITY;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final SagraSettings settings;
    private final UsersRepository usersRepository;
    private final EntityManager em;
    private final DiscountService discountService;

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new SagraNotFoundException("Nessun ordine trovato con l'id: " + orderId));
    }

    @Transactional(rollbackFor = Throwable.class)
    public Order
    createOrder(OrderRequest orderRequest) {

        validateOrderRequest(orderRequest);
        final Order order = orderMapper.toEntity(orderRequest);
        getDiscountRate(orderRequest).ifPresent(order::setDiscountRate);
        updateService(order, orderRequest);

        // FIXME manca gestione dell'utente
        order.setUser(usersRepository.findById("lorenzo").orElseThrow(() -> new RuntimeException("User not found")));

        orderRequest.getOrderedProducts().forEach(orderProductRequest -> addProductToOrder(order, orderProductRequest));
        order.setTotalAmount(calculateTotalAmount(order));

        return orderRepository.save(order);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteOrder(Long orderId) {
        final Order order = getOrderById(orderId);

        order.getOrderedProducts().forEach(op -> {
            if ( ! productService.updateProductQuantity(op.getProduct(), op.getQuantity()) ) {
                log.error("Nella cancellazione di un ordine non dovrebbe mai esserci un errore durante la restituzione della quantità: {}, {}", order, op);
                throw new RuntimeException("Si è verificato un errore inatteso nella cancellazione dell'ordine: " + orderId);
            }
        });

        orderRepository.delete(order);
    }

    @Transactional(rollbackFor = Throwable.class)
    public Order updateOrder(Long orderId, OrderRequest orderRequest) {
        final Order order = getOrderById(orderId);
        validateOrderRequest(orderRequest);

        order.setCustomer(orderRequest.getCustomer());
        order.setNote(orderRequest.getNote());
        order.setTakeAway(orderRequest.isTakeAway());
        getDiscountRate(orderRequest).ifPresent(order::setDiscountRate);

        updateService(order, orderRequest);

        final Map<Long, OrderProduct> orderedProductsMap = order.getOrderedProducts().stream().collect(Collectors.toMap(OrderProduct::getId, Function.identity()));

        for (final OrderProductRequest orderProductRequest : orderRequest.getOrderedProducts()) {
            final OrderProduct orderProduct = orderedProductsMap.get(orderProductRequest.getProductId());

            // Nuovo prodotto da aggiungere
            if ( orderProduct == null ) {
                log.debug("Prodotto da aggiungere all'ordine: orderId={}, {}", orderId, orderProductRequest);
                addProductToOrder(order, orderProductRequest);
                continue;
            }

            // Quantita modificata
            if ( ! orderProduct.getQuantity().equals(orderProductRequest.getQuantity() )) {
                final int diff = orderProduct.getQuantity() - orderProductRequest.getQuantity();
                final Product product = orderProduct.getProduct();
                log.debug("Quantità modificata per prodotto in ordine: orderId={}, productId={}, quantita orig {} - nuova quantita {} = {}", orderId, orderProduct.getProduct().getId(), orderProduct.getQuantity(), orderProductRequest.getQuantity(), diff);

                if ( ! productService.updateProductQuantity(product, diff) ) {
                    throw new SagraQuantitaNonSufficiente(InvalidProduct.of(product.getId(), NOT_ENOUGH_QUANTITY));
                }
            }
        }

        final Set<Long> newOrderedProductsSet = orderRequest.getOrderedProducts().stream().map(OrderProductRequest::getProductId).collect(Collectors.toSet());
        final List<Integer> idxToRemove = new ArrayList<>();
        // Individuiamo i prodotti da rimuovere dall'ordine
        for ( int idx = 0; idx < order.getOrderedProducts().size(); ++idx ) {
            final OrderProduct orderProduct = order.getOrderedProducts().get(idx);

            if ( ! newOrderedProductsSet.contains(orderProduct.getProduct().getId()) ) {
                log.debug("Modifica ordine, individuato prodotto da rimuovere: ordineId={}, {}", orderId, idx);
                idxToRemove.add(idx);
            }
        }

        // Rimuoviamo i prodotti dall'ordine
        idxToRemove.forEach(index -> {
            final OrderProduct removed = order.getOrderedProducts().remove(index.intValue());
            if ( removed == null ) {
                log.error("Il prodotto ordinato individuato per l'eliminzaione non è stato trovato: orderId={}, index prodotto da rimuovere={}", order.getId(), index);
                throw new RuntimeException("Errore durante la modifica di un ordine per rimozione di un prodotto ordinato");
            }

            log.debug("Modifica ordine rimozione prodotto: ordineId={}, index={}, removed={}", orderId, index, removed);
            productService.updateProductQuantity(removed.getProduct(), removed.getQuantity());
        });

        order.setTotalAmount(calculateTotalAmount(order));

        return orderRepository.save(order);
    }

    public List<Order> searchOrders(SearchOrderRequest searchOrderRequest, Pageable pageable) {
        final QOrder o = QOrder.order;
        final JPAQuery<Order> query = new JPAQuery<Order>(em)
                .select(o)
                .from(o);

        if ( searchOrderRequest.getCustomer() != null && ! searchOrderRequest.getCustomer().isEmpty()) {
            query.where(o.customer.containsIgnoreCase(searchOrderRequest.getCustomer()));
        }

        if ( searchOrderRequest.getUsername() != null && ! searchOrderRequest.getUsername().isEmpty()) {
            query.where(o.user.username.eq(searchOrderRequest.getUsername()));
        }

        if ( searchOrderRequest.getCreated() != null ) {
            final LocalDateTime startDate = searchOrderRequest.getCreated().atStartOfDay();
            final LocalDateTime endDate = searchOrderRequest.getCreated().plusDays(1).atStartOfDay();
            query.where(o.created.goe(startDate).and(o.created.lt(endDate)));
        }

        query.offset(pageable.getOffset());
        query.limit(pageable.getPageSize());

        return query.fetch();
    }

    private void validateOrderRequest(OrderRequest orderRequest) {

        final Set<Long> orderedProductsSet = orderRequest.getOrderedProducts().stream().map(OrderProductRequest::getProductId).collect(Collectors.toSet());
        if ( orderedProductsSet.size() != orderRequest.getOrderedProducts().size() ) {
            throw new SagraBadRequestException("Sono presenti dei prodotti inseriti più volte nell'ordine");
        }

        if ( orderRequest.isTakeAway() && orderRequest.getServiceNumber() > 0 ) {
            log.debug("Tentativo di creare/aggiornare un ordine da asporto con indicazione dei coperti: {}", orderRequest);
            throw new SagraBadRequestException(
                    InvalidValue.builder().field("serviceNumber").message("Ordine da asporto non può avere dei coperti").build()
            );
        }
    }

    private void updateService(Order order, OrderRequest orderRequest) {
        if ( ! order.isTakeAway() && order.getServiceNumber() > 0 ) {
            if ( settings.getServiceCost().compareTo(BigDecimal.ZERO) > 0 ) {
                order.setServiceCost(
                        settings.getServiceCost().multiply(new BigDecimal(orderRequest.getServiceNumber()))
                );
            } else {
                order.setServiceCost(BigDecimal.ZERO);
            }
        }
    }

    private  static final BigDecimal ONE_HUNDRED = new BigDecimal(100);
    private BigDecimal calculateTotalAmount(Order order) {
        final BigDecimal total = order.getOrderedProducts().stream().map(op -> op.getProduct().getPrice().multiply(new BigDecimal(op.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .add(order.getServiceCost());

        if ( order.getDiscountRate() != null ) {
            return total.subtract(total.multiply(order.getDiscountRate()).divide(ONE_HUNDRED, RoundingMode.HALF_DOWN).setScale(2, RoundingMode.HALF_DOWN));
        }

        return total;
    }

    private void addProductToOrder(Order order, OrderProductRequest orderProductRequest) {
        final Product product;
        try {
            product = productService.findById(orderProductRequest.getProductId());
        } catch ( SagraNotFoundException e) {
            throw new SagraBadRequestException(
                    InvalidValue.builder().field("productId").message("Prodotto non trovato").value(orderProductRequest.getProductId()).build()
            );
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

    private Optional<BigDecimal> getDiscountRate(OrderRequest orderRequest) {
        if ( orderRequest.getDiscountId() != null ) {
            try {
                final Discount discount = discountService.findById(orderRequest.getDiscountId());
                return Optional.of(discount.getRate());
            } catch (SagraNotFoundException e) {
                throw new SagraBadRequestException(
                        InvalidValue.builder().field("discountId").value(orderRequest.getDiscountId()).message("Codice sconto non trovato").build()
                );
            }
        }

        return Optional.empty();
    }
}
