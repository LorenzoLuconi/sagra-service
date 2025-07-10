package it.loreluc.sagraservice.order;

import lombok.Data;

@Data
public class SearchOrderParams {
    private String customer;
    private Long user;
}
