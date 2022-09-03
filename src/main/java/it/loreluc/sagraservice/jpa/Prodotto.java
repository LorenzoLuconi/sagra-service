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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "prodotti")
@Getter
@Setter
@ToString
@EntityListeners(AuditingEntityListener.class)
public class Prodotto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Length(max = 255)
    private String nome;

    @Length(max = 255)
    private String note;

    @ManyToOne
    private Reparto reparto;

    @ManyToOne
    private Menu menu;

    @NotNull
    @Min(0)
    private BigDecimal prezzo;

    @NotNull
    @Column(name = "blocca_vendita")
    private boolean bloccaVendita;

    @CreatedDate
    private LocalDateTime created;

    @LastModifiedDate
    @Column(name = "last_update")
    private LocalDateTime lastupdate;
}
