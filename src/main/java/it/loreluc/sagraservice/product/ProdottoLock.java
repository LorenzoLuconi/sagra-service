package it.loreluc.sagraservice.product;

import it.loreluc.sagraservice.jpa.Product;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Deprecated
@ApplicationScope
public class ProdottoLock {

    private static Set<Long> usedKeys= ConcurrentHashMap.newKeySet();

    public boolean tryLock(Product product) {
        Objects.requireNonNull(product);
        Objects.requireNonNull(product.getId());
        return usedKeys.add(product.getId());
    }

    public void unlock(Product request) {
        Objects.requireNonNull(request);
        Objects.requireNonNull(request.getId());
        usedKeys.remove(request.getId());
    }
}
