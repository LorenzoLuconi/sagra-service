package it.loreluc.sagraservice.order.resource;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderProductResponse {

    private Long product;

    private Integer quantity;

    private BigDecimal price;

    private String note;
}
