package it.loreluc.sagraservice.security.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(name = "LoginRequest", description = "Credenziali per il login")
public class LoginRequest {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String username;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String password;
}
