package it.loreluc.sagraservice.monitor.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Schema(name="Monitor")
public class MonitorResource {
    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private List<Long> products = new ArrayList<>();
}
