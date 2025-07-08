package it.loreluc.sagraservice.order;

import it.loreluc.sagraservice.error.SagraBadRequestException;
import it.loreluc.sagraservice.error.SagraNotFoundException;
import it.loreluc.sagraservice.jpa.Order;
import it.loreluc.sagraservice.jpa.OrderProduct;
import it.loreluc.sagraservice.jpa.Product;
import it.loreluc.sagraservice.order.resource.OrdineProdottoRequest;
import it.loreluc.sagraservice.order.resource.OrdineRequest;
import it.loreluc.sagraservice.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrdineService {

    private final OrdineMapper ordineMapper;
    private final OrdineRepository ordineRepository;
    private final ProductService productService;

    public Order getOrdineById(Long ordineId) {
        return ordineRepository.findById(ordineId).orElseThrow(() -> new SagraNotFoundException("Nessun ordine trovato con l'id: " + ordineId));
    }

    @Transactional(rollbackFor = Throwable.class)
    // TODO manca risposta
    public void createOrdine(OrdineRequest ordineRequest) {

        // TODO verificare se magazzino per oggi inizializzato

        if ( ordineRequest.isAsporto() && ordineRequest.getCoperti() > 0 ) {
            log.warn("Tentativo di creare un ordine da asporto con indicazione dei coperti: {}", ordineRequest);
            throw new SagraBadRequestException("Ordine da asporto non può avere dei coperti");
        }

        if (! ordineRequest.isAsporto() ) {
            // TODO costo coperti da dove lo prendiamo? Configurazione dell'evento
        }

        final Order order = ordineMapper.mapToOrdine(ordineRequest);

        // FIXME manca gestione dell'utente

        for (final OrdineProdottoRequest ordineProdottoRequest : ordineRequest.getProdotti()) {
            // FIXME Il prodotto va cercato riferito al magazzino
            final Product product = productService.findById(ordineProdottoRequest.getId());

            final OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrder(order);

            orderProduct.setProduct(product);
            orderProduct.setPrice(product.getPrice());
            orderProduct.setQuantity(ordineProdottoRequest.getQuantita());
            orderProduct.setNote(orderProduct.getNote());
            order.getOrderedProducts().add(orderProduct);
        }

        ordineRepository.save(order);
    }
}
