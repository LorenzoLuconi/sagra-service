package it.loreluc.sagraservice.product.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(name = "ProductQuantityInit")
public class ProductQuantityInitRequest {

    @NotNull
    private Long  productId;

    @NotNull @Min(0)
    private Integer initialQuantity;
}
