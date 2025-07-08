package it.loreluc.sagraservice.product;

import lombok.Data;

@Data
public class ProductSearchRequest {

    private Long menu;
    private Long department;
    private String name;
}
