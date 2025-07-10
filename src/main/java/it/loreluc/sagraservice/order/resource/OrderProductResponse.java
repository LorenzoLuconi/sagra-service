package it.loreluc.sagraservice.order.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(name = "OrderedProduct")
public class OrderProductResponse {

    private Long productId;

    private Integer quantity;

    private BigDecimal price;

    private String note;
}
