package it.loreluc.sagraservice.jpa;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "monitors")
@Getter
@Setter
@ToString
public class Monitor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Length(max = 255)
    private String name;

    @OrderBy("idx")
    @OneToMany(mappedBy = "monitor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<MonitorProduct> products = new ArrayList<>();

    @Transient
    public List<Long> getProductIds() {
        if ( products == null) {
            return null;
        }

        return products.stream().map(mp -> mp.getProduct().getId()).toList();
    }
}
