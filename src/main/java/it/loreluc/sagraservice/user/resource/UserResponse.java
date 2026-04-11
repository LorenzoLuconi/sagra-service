package it.loreluc.sagraservice.user.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import it.loreluc.sagraservice.jpa.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(name = "User", description = "Utente applicativo")
public class UserResponse {
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private Role role;
}
