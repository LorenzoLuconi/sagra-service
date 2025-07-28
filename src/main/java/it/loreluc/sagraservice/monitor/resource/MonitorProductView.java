package it.loreluc.sagraservice.monitor.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MonitorProductView {
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer initialQuantity;
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer availableQuantity;
}
