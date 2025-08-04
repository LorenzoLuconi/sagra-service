package it.loreluc.sagraservice.order.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Schema(name = "StatsOrderTakeAway", description = "Statistiche sugli ordini da asporto")
@NoArgsConstructor
@AllArgsConstructor
public class StatOrderTakeAway {
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "Numero di ordini da asporto")
    private Long count;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "Totale importo degli ordini da asport")
    private BigDecimal totalAmount;
}
