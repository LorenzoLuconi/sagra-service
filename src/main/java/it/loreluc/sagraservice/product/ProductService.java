package it.loreluc.sagraservice.product;

import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import it.loreluc.sagraservice.error.SagraConflictException;
import it.loreluc.sagraservice.error.SagraNotFoundException;
import it.loreluc.sagraservice.jpa.Product;
import it.loreluc.sagraservice.jpa.ProductQuantity;
import it.loreluc.sagraservice.jpa.QOrderProduct;
import it.loreluc.sagraservice.jpa.QProduct;
import it.loreluc.sagraservice.product.resource.ProductMapper;
import it.loreluc.sagraservice.product.resource.ProductRequest;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
        final JPAQuery<Product> query = new JPAQuery<Product>(entityManager)
                .select(p)
                .from(p);

        if ( searchRequest.getName() != null ) {
            query.where(p.name.containsIgnoreCase(searchRequest.getName()));
        }

        if ( searchRequest.getDepartmentId() != null ) {
            query.where(p.department.id.eq(searchRequest.getDepartmentId()));
        }

        if ( searchRequest.getCourseId() != null ) {
            query.where(p.course.id.eq(searchRequest.getCourseId()));
        }

        return query.fetch();
    }

    @Transactional(rollbackFor = Throwable.class) // Da valutare la propagazione
    public Product create(ProductRequest productRequest) {
        final Product product = productMapper.toEntity(productRequest);

        if (productRepository.existsByNameIgnoreCase(product.getName())) {
            throw new SagraConflictException(String.format("Prodotto con il nome '%s' già esistente", productRequest.getName()));
        }

        final ProductQuantity productQuantity = new ProductQuantity();
        productQuantity.setProduct(product);
        productQuantity.setQuantity(0);

        product.setProductQuantity(productQuantity);

        return productRepository.save(product);
    }

    @Transactional(rollbackFor = Throwable.class)
    public Product update(Long productId, ProductRequest productRequest) {
        final Product product = findById(productId);
        productMapper.update(product, productRequest);

        return productRepository.save(product);
    }

    @Transactional(rollbackFor = Throwable.class)
    public boolean updateProductQuantity(Long prodottoId, Integer quantityVariation) {
        return updateProductQuantity(findById(prodottoId), quantityVariation);
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.MANDATORY)
    public boolean updateProductQuantity(Product product, Integer quantityVariation) {
        final int updated = entityManager.createQuery("""
            update ProductQuantity pq set pq.quantity = pq.quantity + :quantityVariation
            where pq.product.id = :prodottoId and pq.quantity + :quantityVariation >= 0
            """)
                .setParameter("prodottoId", product.getId())
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
            throw new SagraNotFoundException(String.format("Impossibile rimuovere il prodotto in quanto è referenziato in %s ordini", count));
        }

        productRepository.delete(product);
    }
}
