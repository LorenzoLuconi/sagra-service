package it.loreluc.sagraservice.order.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Schema(name = "StatsOrderDepartment", description = "Statistiche sui reparti")
@NoArgsConstructor
@AllArgsConstructor
public class StatOrderDepartment {
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "Id reparto")
    private Long id;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "Totale importo del reparto")
    private BigDecimal totalAmount;
}
