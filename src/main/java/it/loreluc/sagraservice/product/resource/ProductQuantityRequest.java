package it.loreluc.sagraservice.product.resource;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductQuantityRequest {
    @NotNull
    private final Integer quantityVariation;
}
