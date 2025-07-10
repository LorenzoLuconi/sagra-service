package it.loreluc.sagraservice.discount.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(name="Discount", description = "Sconto")
public class DiscountResponse {
    private Long id;
    private String name;
    private BigDecimal rate;
}
