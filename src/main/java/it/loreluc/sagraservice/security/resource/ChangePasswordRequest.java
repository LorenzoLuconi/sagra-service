package it.loreluc.sagraservice.security.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Schema(name = "ChangePasswordRequest", description = "Richiesta di cambio password per l'utente autenticato")
public class ChangePasswordRequest {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String currentPassword;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    @Length(min = 8, max = 128)
    private String newPassword;
}
