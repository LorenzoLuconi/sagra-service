package it.loreluc.sagraservice.order.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Schema(name = "StatsOrderedProducts")
@Data
public class StatOrderProduct {
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private Long productId;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "Totale importo prodotto venduto")
    private BigDecimal totalAmount;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "Quantità venduta del prodotto")
    private Long totalQuantity;
}
