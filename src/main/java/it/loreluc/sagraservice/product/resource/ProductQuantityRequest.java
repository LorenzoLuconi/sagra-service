package it.loreluc.sagraservice.product.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(name = "ProductQuantityUpdate")
public class ProductQuantityRequest {
    @NotNull
    private final Integer quantityVariation;
}
