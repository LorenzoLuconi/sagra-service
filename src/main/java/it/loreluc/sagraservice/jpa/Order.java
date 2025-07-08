package it.loreluc.sagraservice.jpa;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@ToString
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(max = 128)
    private String name;

    @Length(max = 255)
    private String note;

    @NotNull
    private boolean takeAway;

    private Integer service;

    private BigDecimal serviceCost;

    @CreatedDate
    private LocalDateTime created;

    @LastModifiedDate
    private LocalDateTime lastUpdate;

    // TODO aggiungere gestione sconto

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @NotEmpty
    private List<OrderProduct> orderedProducts = new ArrayList<>();

    @ManyToOne
    @NotNull
    private User user;

}
