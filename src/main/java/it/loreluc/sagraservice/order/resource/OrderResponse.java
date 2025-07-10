package it.loreluc.sagraservice.order.resource;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderResponse {

    private Long id;

    private String customer;

    private String note;

    private boolean takeAway;

    private Integer serviceNumber;

    private BigDecimal serviceCost;

    private BigDecimal totalAmount;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime created;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastUpdate;

    private List<OrderProductResponse> orderedProducts = new ArrayList<>();
}
