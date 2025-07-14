package it.loreluc.sagraservice.monitor.resource;

import lombok.Data;

@Data
public class MonitorProductView {
    private String name;
    private Integer initialQuantity;
    private Integer availableQuantity;
}
