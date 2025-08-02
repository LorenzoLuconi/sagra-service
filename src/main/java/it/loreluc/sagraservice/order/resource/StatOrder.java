package it.loreluc.sagraservice.order.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Collection;

@Schema(name = "StatsOrder")
@Data
public class StatOrder {
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "Numero totale dei coperti")
    private Long totalServiceNumber;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "Totale ammontare degli ordini")
    private BigDecimal totalAmount;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "Numero di ordini registrati")
    private Long count;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "Dettagli dei prodotti ordinati")
    private Collection<StatOrderProduct> products;
}
