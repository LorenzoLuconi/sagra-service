package it.loreluc.sagraservice.product;

import lombok.Data;

@Data
public class ProductSearchRequest {

    private Long courseId;
    private Long departmentId;
    private String name;
}
