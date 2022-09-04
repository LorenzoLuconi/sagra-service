package it.loreluc.sagraservice.jpa;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "prodotti_ordinati")
@Getter
@Setter
@ToString
@EntityListeners(AuditingEntityListener.class)
public class ProdottoOrdinato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "ordine")
    private Ordine ordine;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "prodotto")
    private Prodotto prodotto;

    @NotNull
    @Min(0)
    private BigDecimal prezzo;

    @NotNull
    @Min(0)
    private Integer quantita;

    @Length(max=255)
    private String note;

    @CreatedDate
    private LocalDateTime created;

    @LastModifiedDate
    @Column(name = "last_update")
    private LocalDateTime lastupdate;
}
