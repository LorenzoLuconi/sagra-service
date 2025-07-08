package it.loreluc.sagraservice.jpa;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of="id")
@Table(name = "products_quantity")
public class ProductQuantity {

    @Id
    @Column(name="product")
    private Long id;

    @NotNull
    @MapsId
    @OneToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "product")
    private Product product;

    private Integer quantity;

}
