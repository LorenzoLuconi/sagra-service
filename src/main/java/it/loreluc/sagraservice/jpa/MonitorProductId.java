package it.loreluc.sagraservice.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Embeddable @NoArgsConstructor @AllArgsConstructor
public class MonitorProductId implements Serializable {
    @Column(name = "monitor_id")
    private Long monitorId;
    @Column(name = "product_id")
    private Long productId;
}
