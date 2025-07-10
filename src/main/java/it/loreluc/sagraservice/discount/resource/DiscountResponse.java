package it.loreluc.sagraservice.discount.resource;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DiscountResponse {
    private Long id;
    private String name;
    private BigDecimal rate;
}
