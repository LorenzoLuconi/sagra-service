package it.loreluc.sagraservice.product;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import it.loreluc.sagraservice.error.InvalidValue;
import it.loreluc.sagraservice.error.SagraBadRequestException;
import it.loreluc.sagraservice.error.SagraConflictException;
import it.loreluc.sagraservice.error.SagraNotFoundException;
import it.loreluc.sagraservice.jpa.*;
import it.loreluc.sagraservice.product.resource.ProductMapper;
import it.loreluc.sagraservice.product.resource.ProductRequest;
import it.loreluc.sagraservice.product.resource.ProductResponse;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final EntityManager entityManager;
    private final ProductMapper productMapper;

    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new SagraNotFoundException("Prodotto non trovato con id: " + id));
    }

    public List<Product> search(ProductSearchRequest searchRequest) {
        final QProduct p = QProduct.product;

        final BooleanBuilder booleanBuilder = new BooleanBuilder();

        if ( searchRequest.getName() != null ) {
            booleanBuilder.and(p.name.containsIgnoreCase(searchRequest.getName()));
        }

        if ( searchRequest.getDepartmentId() != null ) {
            booleanBuilder.and(p.department.id.eq(searchRequest.getDepartmentId()));
        }

        if ( searchRequest.getCourseId() != null ) {
            booleanBuilder.and(p.course.id.eq(searchRequest.getCourseId()));
        }

        if ( searchRequest.isExcludeLinked() ) {
            booleanBuilder.and(p.parentId.isNull());
        }

        final JPAQuery<Product> query = new JPAQuery<Product>(entityManager)
                .select(p)
                .from(p)
                .where(booleanBuilder)
                .orderBy(p.name.asc());

        return query.fetch();
    }

    @Transactional(rollbackFor = Throwable.class) // Da valutare la propagazione
    public Product create(ProductRequest productRequest) {
        final Product product = productMapper.toEntity(productRequest);

        if (productRepository.existsByNameIgnoreCase(product.getName())) {
            throw new SagraConflictException(
                    String.format("Prodotto con il nome '%s' già esistente", productRequest.getName()),
                    InvalidValue.builder().field("name").value(product.getName()).message("Nome già esistente").build()
            );
        }

        if ( productRequest.getParentId() != null ) {
            validateParentProduct(productRequest.getParentId());
            product.setParentId(productRequest.getParentId());
        }

        final ProductQuantity productQuantity = new ProductQuantity();
        productQuantity.setProduct(product);
        productQuantity.setInitialQuantity(0);
        productQuantity.setAvailableQuantity(0);

        product.setProductQuantity(productQuantity);

        return productRepository.save(product);
    }

    @Transactional(rollbackFor = Throwable.class)
    public Product update(Long productId, ProductRequest productRequest) {
        final Product product = findById(productId);

        if (productRepository.existsByNameIgnoreCaseAndIdNot(productRequest.getName(), productId)) {
            throw new SagraConflictException(String.format("Prodotto con il nome '%s' già esistente", productRequest.getName()));
        }

        productMapper.update(product, productRequest);

        return productRepository.save(product);
    }

    @Transactional(rollbackFor = Throwable.class)
    public boolean updateProductQuantityAvailability(Long prodottoId, Integer quantityVariation) {
        final Product product = findById(prodottoId);
        final Long productId;
        if ( product.getParentId() != null ) {
            productId = product.getParentId();
        } else {
            productId = product.getId();
        }

        final int updated = entityManager.createQuery("""
            update ProductQuantity pq set pq.availableQuantity = pq.availableQuantity + :quantityVariation,
             pq.initialQuantity = pq.initialQuantity + :quantityVariation
            where pq.product.id = :prodottoId and pq.availableQuantity + :quantityVariation >= 0
            """)
                .setParameter("prodottoId", productId)
                .setParameter("quantityVariation", quantityVariation)
                .executeUpdate();

        return updated == 1;
    }


    private void checkOrdersForQuantityInit() {
        final LocalDateTime startDate = LocalDate.now().atStartOfDay();
        final LocalDateTime endDate = LocalDate.now().plusDays(1).atStartOfDay();

        final QOrder o = QOrder.order;

        final Long count = new JPAQuery<Long>(entityManager)
                .select(o.count())
                .from(o)
                .where(o.created.goe(startDate).and(o.created.lt(endDate)))
                .fetchOne();

        if ( count != null && count > 0 ) {
            throw new SagraConflictException(String.format("Sono già presenti %s ordini in data odierna, inizializzazione non possibile", count));
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public void initProductQuantity(Long productId, Integer quantityInitialization) {

        if ( quantityInitialization == null || quantityInitialization < 0 ) {
            throw new SagraBadRequestException("La quantità iniziale deve essere maggiore o uguale a 0");
        }

        final Product product = findById(productId);
        checkOrdersForQuantityInit();

        final ProductQuantity productQuantity = product.getProductQuantity();

        if ( product.getParentId() != null ) {
            throw new SagraBadRequestException(String.format("Il prodotto con id %s, è collegato ad un altro prodotto e non può essere inizializzato", product.getId()));
        }

        productQuantity.setInitialQuantity(quantityInitialization);
        productQuantity.setAvailableQuantity(quantityInitialization);
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.MANDATORY)
    public boolean updateProductQuantityForOrder(Product product, Integer quantityVariation) {
        final Long productId;
        if ( product.getParentId() != null ) {
            productId = product.getParentId();
        } else {
            productId = product.getId();
        }

        final int updated = entityManager.createQuery("""
            update ProductQuantity pq set pq.availableQuantity = pq.availableQuantity + :quantityVariation
            where pq.product.id = :prodottoId and pq.availableQuantity + :quantityVariation >= 0
            """)
                .setParameter("prodottoId", productId)
                .setParameter("quantityVariation", quantityVariation)
                .executeUpdate();

        return updated == 1;
    }

    @Transactional(rollbackFor = Throwable.class)
    public Product sellLock(Long productId, boolean locked) {
        final Product product = findById(productId);

        product.setSellLocked(locked);

        return productRepository.save(product);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void delete(Long productId) {
        final Product product = findById(productId);

        final QOrderProduct q = QOrderProduct.orderProduct;
        final Long count = new JPAQuery<Long>(entityManager)
                .select(Wildcard.count).from(q)
                .where(q.product.id.eq(productId)).fetchOne();

        if ( count != null && count > 0 ) {
            throw new SagraConflictException(String.format("Impossibile rimuovere il prodotto in quanto è referenziato in %s ordini", count));
        }

        productRepository.delete(product);
    }

    public ProductResponse toResource(Product product) {
        final ProductResponse resource = productMapper.toResource(product);
        if ( product.getParentId() != null ) {
            final Product parent = findById(product.getParentId());
            resource.setInitialQuantity(parent.getProductQuantity().getInitialQuantity());
            resource.setAvailableQuantity(parent.getProductQuantity().getAvailableQuantity());
        } else {
            resource.setAvailableQuantity(product.getProductQuantity().getAvailableQuantity());
            resource.setInitialQuantity(product.getProductQuantity().getInitialQuantity());
        }

        return resource;
    }

    private void validateParentProduct(Long parentId) {
        final Product parent = productRepository.findById(parentId).orElseThrow(() ->
                new SagraBadRequestException(InvalidValue.builder()
                        .field("parentId")
                        .value(parentId)
                        .message("Prodotto collegato non trovato")
                        .build()
                )
        );

        if ( parent.getParentId() != null ) {
            throw new SagraBadRequestException(InvalidValue.builder()
                    .field("parentId")
                    .value(parentId)
                    .message("Il prodotto collegato non può essere collegato a sua volta ad un'altro prodotto")
                    .build()
            );
        }
    }
}
