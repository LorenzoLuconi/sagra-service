package it.loreluc.sagraservice.jpa;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "monitors_products")
@Getter
@Setter
@ToString(of = {"id", "idx"})
public class MonitorProduct {

    @EmbeddedId
    private MonitorProductId id;

    @NotNull
    @ManyToOne
    @MapsId("monitorId")
    @JoinColumn(name = "monitor_id")
    private Monitor monitor;

    @NotNull
    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    @NotNull @Min(0)
    private Short idx;
}
