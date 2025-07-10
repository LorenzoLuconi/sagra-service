package it.loreluc.sagraservice.jpa;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
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

@Entity
@Table(name = "products")
@Getter
@Setter
@ToString
@EntityListeners(AuditingEntityListener.class)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Length(max = 255)
    private String name;

    @Length(max = 255)
    private String note;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @NotNull
    @Min(0)
    private BigDecimal price;

    private boolean sellLocked = false;

    @NotNull
    @OneToOne(fetch = FetchType.EAGER, mappedBy = "product", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private ProductQuantity productQuantity;

    @CreatedDate
    private LocalDateTime created;

    @LastModifiedDate
    private LocalDateTime lastUpdate;
}
