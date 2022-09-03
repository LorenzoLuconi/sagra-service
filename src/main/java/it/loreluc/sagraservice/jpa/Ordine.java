package it.loreluc.sagraservice.jpa;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ordini")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@ToString
public class Ordine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(max = 128)
    private String nome;

    @Length(max = 255)
    private String note;

    @NotNull
    private boolean asporto;

    private Integer coperti;

    @Column(name = "costo_coperti")
    private BigDecimal costoCoperti;

    @CreatedDate
    private LocalDateTime created;

    @LastModifiedDate
    @Column(name = "last_update")
    private LocalDateTime lastupdate;

}
