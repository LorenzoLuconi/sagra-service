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


    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String customer;

    private String note;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private boolean takeAway;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer serviceNumber;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal serviceCost;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal totalAmount;

    private BigDecimal discountRate;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime created;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime lastUpdate;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private List<OrderProductResponse> products = new ArrayList<>();
}
