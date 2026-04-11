package it.loreluc.sagraservice.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@Data
@Component
@Validated
@ConfigurationProperties(prefix = "sagra-service")
public class SagraSettings {
    @NotNull
    @Min(0) // In teoria dovrebbe essere legato all'evento che non esiste ancora
    private BigDecimal serviceCost;

    @NotNull @Valid
    private DefaultAdmin defaultAdmin = new DefaultAdmin();

    @Data
    public static class DefaultAdmin {
        @NotBlank
        private String username;

        @NotBlank
        private String name;

        @NotBlank
        private String password;
    }
}
