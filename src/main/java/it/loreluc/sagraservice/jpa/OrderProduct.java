package it.loreluc.sagraservice.jpa;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

@Entity
@Table(name = "orders_products")
@Getter
@Setter
@ToString(of = {"orderId", "productId", "quantity","idx"})
@IdClass(OrderProductId.class)
@EqualsAndHashCode(of = {"orderId","productId"})
public class OrderProduct {

    @Id
    @Column(name = "order_id")
    private Long orderId;

    @Id
    @Column(name = "product_id")
    private Long productId;

    @NotNull
    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private Order order;

    @NotNull
    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    @NotNull
    @Min(0)
    private BigDecimal price;

    @NotNull
    @Min(1)
    private Integer quantity;

    @Length(max=255)
    private String note;

    @NotNull
    private Integer idx;
}
