package it.loreluc.sagraservice.order.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(name = "OrderedProduct")
public class OrderProductResponse {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private Long productId;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer quantity;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal price;

    private String note;
}
