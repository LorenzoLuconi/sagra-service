package it.loreluc.sagraservice.config;

import jakarta.validation.constraints.Min;
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

    /**
     * Duranta in minuti del token
     */
    @NotNull @Min(0)
    private Long tokenExpire;
}
