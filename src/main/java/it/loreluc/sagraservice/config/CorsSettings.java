package it.loreluc.sagraservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Data
@Component
@Validated
@ConfigurationProperties(prefix = "sagra-service.cors")
public class CorsSettings {

    private String[] allowedOrigins;

    private String[] allowedMethods;

    private Long maxAge;
}
