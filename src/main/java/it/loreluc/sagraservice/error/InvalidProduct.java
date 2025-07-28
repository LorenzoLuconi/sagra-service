package it.loreluc.sagraservice.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
public class InvalidProduct {
    @RequiredArgsConstructor
    public enum ProductError {
        NOT_ENOUGH_QUANTITY("Quantità prodotto non sufficiente"),
        LOCKED("Prodotto non disponibile per la vendita")
        ;

        @Getter
        private final String message;
    }

    @Schema(requiredMode =  Schema.RequiredMode.REQUIRED)
    private final Long productId;

    @Schema(requiredMode =  Schema.RequiredMode.REQUIRED)
    private final String message;

    @Schema(requiredMode =  Schema.RequiredMode.REQUIRED)
    private final ProductError error;

    private InvalidProduct(Long productId, ProductError productError) {
        this.productId = productId;
        this.error = productError;
        this.message = productError.getMessage();
    }

    public static InvalidProduct of(Long productId, ProductError productError) {
        return new InvalidProduct(productId, productError);
    }
}
