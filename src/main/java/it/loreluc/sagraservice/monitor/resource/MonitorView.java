package it.loreluc.sagraservice.monitor.resource;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MonitorView {

    private String name;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime lastUpdate = LocalDateTime.now();
    private List<MonitorProductView> products;
}
