package it.loreluc.sagraservice.product;

import com.querydsl.jpa.impl.JPAQuery;
import it.loreluc.sagraservice.error.SagraNotFoundException;
import it.loreluc.sagraservice.jpa.Product;
import it.loreluc.sagraservice.jpa.ProductQuantity;
import it.loreluc.sagraservice.jpa.QProduct;
import it.loreluc.sagraservice.product.resource.ProductMapper;
import it.loreluc.sagraservice.product.resource.ProductRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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
        return productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Prodotto non trovato con id: " + id));
    }

    public List<Product> search(ProductSearchRequest searchRequest) {
        final QProduct p = QProduct.product;
        final JPAQuery<Product> query = new JPAQuery<Product>(entityManager)
                .select(p)
                .from(p);

        if ( searchRequest.getName() != null ) {
            query.where(p.name.containsIgnoreCase(searchRequest.getName()));
        }

        if ( searchRequest.getDepartment() != null ) {
            query.where(p.department.id.eq(searchRequest.getDepartment()));
        }

        if ( searchRequest.getMenu() != null ) {
            query.where(p.menu.id.eq(searchRequest.getMenu()));
        }

        return query.fetch();
    }

    @Transactional(rollbackFor = Throwable.class) // Da valutare la propagazione
    public Product create(ProductRequest productRequest) {
        final Product product = productMapper.toEntity(productRequest);

        final ProductQuantity productQuantity = new ProductQuantity();
        productQuantity.setProduct(product);
        productQuantity.setQuantity(0);

        product.setProductQuantity(productQuantity);

        return productRepository.save(product);
    }

    @Transactional(rollbackFor = Throwable.class) // Da valutare la propagazione
    public Product update(Long productId, ProductRequest productRequest) {
        final Product product = findById(productId);
        productMapper.update(product, productRequest);

        return productRepository.save(product);
    }

    @Transactional(rollbackFor = Throwable.class) // Da valutare la propagazione
    public boolean updateQuantity(Long prodottoId, Integer quantitaRichiesta) {
        final Product product = productRepository.findById(prodottoId).orElseThrow(() -> new SagraNotFoundException("Prodotto non trovato: id=" + prodottoId));
        return updateQuantita(product, quantitaRichiesta);
    }

    @Transactional(rollbackFor = Throwable.class)
    public boolean updateQuantita(Product product, Integer aggQuantita) {
        return false;

//        final int updated = entityManager.createQuery("update DisponibilitaProdotti m set m.quantita = m.quantita + :aggQuantita where m.id = :prodottoId and m.quantita + :aggQuantita >= 0")
//                .setParameter("prodottoId", product.getId())
//                .setParameter("aggQuantita", aggQuantita)
//                .executeUpdate();
//
//            return updated == 1;
    }
}
