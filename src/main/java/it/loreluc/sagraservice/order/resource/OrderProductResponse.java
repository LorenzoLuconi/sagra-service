package it.loreluc.sagraservice.order.resource;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderProductResponse {

    private Long productId;

    private Integer quantity;

    private BigDecimal price;

    private String note;
}
