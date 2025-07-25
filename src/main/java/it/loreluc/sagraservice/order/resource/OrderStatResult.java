package it.loreluc.sagraservice.order.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class OrderStatResult {
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate date;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private Long serviceNumber;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal totalAmount;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private Long count;
}
