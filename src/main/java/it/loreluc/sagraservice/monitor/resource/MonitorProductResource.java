package it.loreluc.sagraservice.monitor.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(name="MonitorProduct")
public class MonitorProductResource {
    @NotNull
    private Long productId;

    @NotNull @Min(0)
    private Short priority;
}
