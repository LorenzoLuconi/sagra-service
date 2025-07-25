package it.loreluc.sagraservice.order.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderedProductsStats {
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private Long productId;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal totalAmount;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private Long count;
}
