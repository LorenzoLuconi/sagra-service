package it.loreluc.sagraservice.jpa;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(
        name = "app_configurations",
        uniqueConstraints = @UniqueConstraint(name = "uk_app_configurations_group_key", columnNames = {"group_name", "config_key"})
)
@Getter
@Setter
@ToString
public class AppConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Length(max = 64)
    @Column(name = "group_name", nullable = false, length = 64)
    private String groupName;

    @NotEmpty
    @Length(max = 64)
    @Column(name = "config_key", nullable = false, length = 64)
    private String key;

    @Lob
    @Column(name = "config_value")
    private String value;
}
