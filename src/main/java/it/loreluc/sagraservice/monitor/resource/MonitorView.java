package it.loreluc.sagraservice.monitor.resource;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MonitorView {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime lastUpdate = LocalDateTime.now();

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private List<MonitorProductView> products;
}
