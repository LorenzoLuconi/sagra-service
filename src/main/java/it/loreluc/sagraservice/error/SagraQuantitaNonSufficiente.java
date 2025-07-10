package it.loreluc.sagraservice.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class SagraQuantitaNonSufficiente extends RuntimeException {
    @Getter
    private final List<InvalidProduct> invalidProducts;

    public SagraQuantitaNonSufficiente(InvalidProduct invalidProduct) {
        this.invalidProducts = Collections.singletonList(invalidProduct);
    }


}
