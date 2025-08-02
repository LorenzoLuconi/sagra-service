package it.loreluc.sagraservice.order;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import it.loreluc.sagraservice.config.SagraSettings;
import it.loreluc.sagraservice.discount.DiscountService;
import it.loreluc.sagraservice.error.*;
import it.loreluc.sagraservice.jpa.*;
import it.loreluc.sagraservice.order.resource.OrderProductRequest;
import it.loreluc.sagraservice.order.resource.OrderRequest;
import it.loreluc.sagraservice.order.resource.StatOrder;
import it.loreluc.sagraservice.order.resource.StatOrderProduct;
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
import java.time.LocalDate;
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
    private final EntityManager entityManager;
    private final DiscountService discountService;

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new SagraNotFoundException("Nessun ordine trovato con l'id: " + orderId));
    }

    @Transactional(rollbackFor = Throwable.class)
    public Order createOrder(OrderRequest orderRequest) {

        validateOrderRequest(orderRequest);
        final Order order = orderMapper.toEntity(orderRequest);
        //getDiscountRate(orderRequest).ifPresent(order::setDiscountRate);
        order.setServiceCost(settings.getServiceCost());

        // FIXME manca gestione dell'utente
        order.setUser(usersRepository.findById("lorenzo").orElseThrow(() -> new RuntimeException("User not found")));

        int idx = 0;
        for (OrderProductRequest orderProductRequest : orderRequest.getProducts()) {
            addProductToOrder(order, orderProductRequest, idx++);
        }

        order.setTotalAmount(calculateTotalAmount(order));

        return orderRepository.save(order);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteOrder(Long orderId) {
        final Order order = getOrderById(orderId);

        order.getProducts().forEach(op -> {
            if ( ! productService.updateProductQuantityForOrder(op.getProduct(), op.getQuantity()) ) {
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

        orderMapper.updateEntity(order, orderRequest);
//        final Optional<BigDecimal> discountRateOptional = getDiscountRate(orderRequest);
//        if  (discountRateOptional.isPresent()) {
//            order.setDiscountRate(discountRateOptional.get());
//        } else {
//            order.setDiscountRate(null);
//        }

        final Map<Long, OrderProduct> orderedProductsMap = order.getProducts().stream()
                .collect(Collectors.toMap(o -> o.getProduct().getId(), Function.identity()));

        for (int i = 0; i < orderRequest.getProducts().size(); i++) {
            final OrderProductRequest orderProductRequest = orderRequest.getProducts().get(i);
            final OrderProduct orderProduct = orderedProductsMap.get(orderProductRequest.getProductId());

            // Nuovo prodotto da aggiungere
            if ( orderProduct == null ) {
                log.debug("Prodotto da aggiungere all'ordine: orderId={}, {}", orderId, orderProductRequest);
                addProductToOrder(order, orderProductRequest, i);
                order.setLastUpdate(LocalDateTime.now());
                continue;
            }

            // Quantita modificata
            if ( ! orderProduct.getQuantity().equals(orderProductRequest.getQuantity() )) {
                final int diff = orderProduct.getQuantity() - orderProductRequest.getQuantity();
                final Product product = orderProduct.getProduct();
                log.debug("Quantità modificata per prodotto in ordine: orderId={}, productId={}, quantita orig {} - nuova quantita {} = {}", orderId, orderProduct.getProduct().getId(), orderProduct.getQuantity(), orderProductRequest.getQuantity(), diff);

                if ( ! productService.updateProductQuantityForOrder(product, diff) ) {
                    throw new SagraQuantitaNonSufficiente(InvalidProduct.of(product.getId(), NOT_ENOUGH_QUANTITY));
                }
                orderProduct.setQuantity(orderProductRequest.getQuantity());
                order.setLastUpdate(LocalDateTime.now());
            }

            orderProduct.setIdx(i);
        }

        final Set<Long> newOrderedProductsSet = orderRequest.getProducts().stream().map(OrderProductRequest::getProductId).collect(Collectors.toSet());
        final List<OrderProduct> productsToRemove = new ArrayList<>();

        // Individuiamo i prodotti da rimuovere
        order.getProducts().forEach(orderProduct -> {
            if ( ! newOrderedProductsSet.contains(orderProduct.getProduct().getId()) ) {
                productsToRemove.add(orderProduct);
            }
        });

        // Rimuoviamo i prodotti dall'ordine
        productsToRemove.forEach(orderProduct -> {
            if ( ! order.getProducts().remove(orderProduct) ) {
                log.error("Il prodotto ordinato individuato per l'eliminazione non è stato trovato: orderId={}, prodotto da rimuovere={}", order.getId(), orderProduct);
                throw new RuntimeException("Errore durante la modifica di un ordine per rimozione di un prodotto ordinato");
            }

            log.debug("Modifica ordine rimozione prodotto: ordineId={}, removed={}", orderId, orderProduct);
            productService.updateProductQuantityForOrder(orderProduct.getProduct(), orderProduct.getQuantity());
            entityManager.remove(orderProduct);
            order.setLastUpdate(LocalDateTime.now());
        });

        order.setTotalAmount(calculateTotalAmount(order));

        if ( order.getProducts().size() != orderRequest.getProducts().size() ) {
            log.error("Dopo l'aggiornamento dell'ordine il numero di elementi tra ordine e orderRequest non corrisponde: {}, {}", orderRequest, order);
            throw new RuntimeException("Non corrispondenza tra numero prodotti ordinati e richiesta aggiornamento ordine");
        }

        return orderRepository.save(order);
    }

    private Predicate createSearchCriteria(SearchOrderRequest searchOrderRequest) {
        final BooleanBuilder booleanBuilder = new BooleanBuilder();
        final QOrder o = QOrder.order;

        if ( searchOrderRequest.getCustomer() != null && ! searchOrderRequest.getCustomer().isEmpty()) {
            booleanBuilder.and(o.customer.containsIgnoreCase(searchOrderRequest.getCustomer()));
        }

        if ( searchOrderRequest.getUsername() != null && ! searchOrderRequest.getUsername().isEmpty()) {
            booleanBuilder.and(o.user.username.eq(searchOrderRequest.getUsername()));
        }

        if ( searchOrderRequest.getCreated() != null ) {
            final LocalDateTime startDate = searchOrderRequest.getCreated().atStartOfDay();
            final LocalDateTime endDate = searchOrderRequest.getCreated().plusDays(1).atStartOfDay();
            booleanBuilder.and(o.created.goe(startDate).and(o.created.lt(endDate)));
        }


        return booleanBuilder;
    }

    public List<Order> searchOrders(SearchOrderRequest searchOrderRequest, Pageable pageable) {
        final QOrder o = QOrder.order;
        final JPAQuery<Order> query = new JPAQuery<Order>(entityManager)
                .select(o)
                .from(o)
                .where(createSearchCriteria(searchOrderRequest))
                .orderBy(o.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                ;

        return query.fetch();
    }

    public Long countOrders(SearchOrderRequest searchOrderRequest) {
        final QOrder o = QOrder.order;
        return new JPAQuery<Order>(entityManager)
                .select(Wildcard.count)
                .from(o)
                .where(createSearchCriteria(searchOrderRequest))
                .fetchOne()
                ;
    }

    private void validateOrderRequest(OrderRequest orderRequest) {

        final Set<Long> orderedProductsSet = orderRequest.getProducts().stream().map(OrderProductRequest::getProductId).collect(Collectors.toSet());
        if ( orderedProductsSet.size() != orderRequest.getProducts().size() ) {
            throw new SagraBadRequestException("Sono presenti dei prodotti inseriti più volte nell'ordine");
        }

        if ( orderRequest.isTakeAway() && orderRequest.getServiceNumber() > 0 ) {
            log.debug("Tentativo di creare/aggiornare un ordine da asporto con indicazione dei coperti: {}", orderRequest);
            throw new SagraBadRequestException(
                    InvalidValue.builder()
                            .field("serviceNumber")
                            .value(orderRequest.getServiceNumber())
                            .message("Ordine da asporto non può avere dei coperti")
                            .build()
            );
        }
    }

    private BigDecimal calculateServiceCost(OrderRequest orderRequest) {
        if ( orderRequest.getServiceNumber() > 0 && settings.getServiceCost().compareTo(BigDecimal.ZERO) > 0) {
            return settings.getServiceCost().multiply(new BigDecimal(orderRequest.getServiceNumber()));
        }
        return BigDecimal.ZERO;
    }

    private  static final BigDecimal ONE_HUNDRED = new BigDecimal(100);
    private BigDecimal calculateTotalAmount(Order order) {
        final BigDecimal total = order.getProducts().stream().map(op -> op.getProduct().getPrice().multiply(new BigDecimal(op.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .add(order.getServiceCost().multiply(new BigDecimal(order.getServiceNumber())));

        if ( order.getDiscountRate() != null ) {
            return total.subtract(total.multiply(order.getDiscountRate()).divide(ONE_HUNDRED, RoundingMode.HALF_DOWN).setScale(2, RoundingMode.HALF_DOWN));
        }

        return total;
    }

    private OrderProduct addProductToOrder(Order order, OrderProductRequest orderProductRequest, int idx) {
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

        if ( ! productService.updateProductQuantityForOrder(product, -orderProductRequest.getQuantity()) ) {
            throw new SagraQuantitaNonSufficiente(InvalidProduct.of(product.getId(), NOT_ENOUGH_QUANTITY));
        }

        final OrderProduct orderProduct = new OrderProduct();
        orderProduct.setOrder(order);
        orderProduct.setOrderId(order.getId());
        orderProduct.setProduct(product);
        orderProduct.setProductId(product.getId());
        orderProduct.setPrice(product.getPrice());
        orderProduct.setQuantity(orderProductRequest.getQuantity());
        orderProduct.setNote(orderProduct.getNote());
        orderProduct.setIdx(idx);
        order.getProducts().add(idx, orderProduct);

        return orderProduct;
    }

//    private Optional<BigDecimal> getDiscountRate(OrderRequest orderRequest) {
//        if ( orderRequest.getDiscountId() != null ) {
//            try {
//                final Discount discount = discountService.findById(orderRequest.getDiscountId());
//                return Optional.of(discount.getRate());
//            } catch (SagraNotFoundException e) {
//                throw new SagraBadRequestException(
//                        InvalidValue.builder().field("discountId").value(orderRequest.getDiscountId()).message("Codice sconto non trovato").build()
//                );
//            }
//        }
//
//        return Optional.empty();
//    }

    public Map<LocalDate, StatOrder> ordersStats(LocalDate date) {
        final QOrder o = QOrder.order;



        final JPAQuery<Tuple> query = new JPAQuery<>(entityManager)
                .select(o.created.year(), o.created.month(), o.created.dayOfMonth(),
                        o.totalAmount.sumBigDecimal(), o.serviceNumber.sumLong(), Wildcard.count)
                .from(o)
                .orderBy(o.created.year().asc(), o.created.month().asc(), o.created.dayOfMonth().asc())
                .groupBy(o.created.year(), o.created.month(), o.created.dayOfMonth());

        if ( date != null ) {
            final LocalDateTime startDate = date.atStartOfDay();
            final LocalDateTime endDate = date.plusDays(1).atStartOfDay();
            query.where((o.created.goe(startDate).and(o.created.lt(endDate))));
        }

        final HashMap<LocalDate, StatOrder> prodMap = new LinkedHashMap<>();
        query.fetch().forEach(t -> {
            final StatOrder result = new StatOrder();
            result.setTotalAmount(t.get(o.totalAmount.sumBigDecimal()));
            result.setCount(t.get(Wildcard.count));
            result.setTotalServiceNumber(t.get(o.serviceNumber.sumLong()));
            final LocalDate localDate = createLocalDate(t);
            result.setProducts(orderedProductsStats(localDate));

            prodMap.putIfAbsent(localDate, result);
        });

        return prodMap;
    }

    public Collection<StatOrderProduct> orderedProductsStats(LocalDate date) {
        final QOrder o = QOrder.order;
        final QOrderProduct op = QOrderProduct.orderProduct;

        final LocalDateTime startDate = date.atStartOfDay();
        final LocalDateTime endDate = date.plusDays(1).atStartOfDay();


        final JPAQuery<Tuple> query = new JPAQuery<>(entityManager)
                .select(o.created.year(), o.created.month(), o.created.dayOfMonth(),
                        op.product.id, op.price.sumBigDecimal(), op.product.parentId, Wildcard.count)
                .from(op)
                .join(op.order, o)
                .where((o.created.goe(startDate).and(o.created.lt(endDate))))
                .orderBy(o.created.year().asc(), o.created.month().asc(), o.created.dayOfMonth().asc(), Wildcard.count.desc())
                .groupBy(o.created.year(), o.created.month(), o.created.dayOfMonth(), op.product.id, op.product.parentId);


        final Map<Long, StatOrderProduct> prodMap = new LinkedHashMap<>();
        query.fetch().forEach(t -> {
            final Long parentId = t.get(op.product.parentId);
            final Long productId = t.get(op.product.id);
            final Long key = parentId == null ? productId : parentId;

            final StatOrderProduct statOrderProduct = prodMap.get(key);

            if ( statOrderProduct == null ) {
                final StatOrderProduct result = new StatOrderProduct();
                result.setTotalAmount(t.get(op.price.sumBigDecimal()));
                result.setCount(t.get(Wildcard.count));
                result.setProductId(key);

                prodMap.put(key, result);
            } else {
                statOrderProduct.setCount(statOrderProduct.getCount() + Objects.requireNonNull(t.get(Wildcard.count)));
                statOrderProduct.setTotalAmount(statOrderProduct.getTotalAmount().add(t.get(op.price.sumBigDecimal())));
            }

        });

        return prodMap.values();
        
    }

    private LocalDate createLocalDate(Tuple t) {
        final QOrder o = QOrder.order;
        final Integer year = t.get(o.created.year());
        final Integer month = t.get(o.created.month());
        final Integer day = t.get(o.created.dayOfMonth());

        if ( year == null || month == null && day == null)
            throw new RuntimeException("Anno, mese o giorno non può essere nulla nell'aggregazione");

        return LocalDate.of(year, month, day);
    }
}
