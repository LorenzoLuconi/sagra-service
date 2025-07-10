package it.loreluc.sagraservice.order.resource;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Schema(name = "Order")
public class OrderResponse {

    private Long id;

    private String customer;

    private String note;

    private boolean takeAway;

    private Integer serviceNumber;

    private BigDecimal serviceCost;

    private BigDecimal totalAmount;

    private BigDecimal discountRate;

    private String username;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime created;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastUpdate;

    private List<OrderProductResponse> orderedProducts = new ArrayList<>();
}
