package it.loreluc.sagraservice.discount.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(name="Discount", description = "Sconto")
public class DiscountResponse {
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal rate;
}
