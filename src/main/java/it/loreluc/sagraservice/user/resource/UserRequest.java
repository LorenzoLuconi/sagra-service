package it.loreluc.sagraservice.user.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import it.loreluc.sagraservice.jpa.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Schema(name = "UserRequest", description = "Dati per la creazione di un utente")
public class UserRequest {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    @Length(min = 6, max = 16)
    private String username;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    @Length(max = 64)
    private String name;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "Ruolo assegnato all'utente")
    @NotNull
    private Role role;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    @Length(min = 8, max = 128)
    private String password;
}
