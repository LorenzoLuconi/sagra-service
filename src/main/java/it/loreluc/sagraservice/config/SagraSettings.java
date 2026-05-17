package it.loreluc.sagraservice.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Data
@Component
@Validated
@ConfigurationProperties(prefix = "sagra-service")
public class SagraSettings {
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
