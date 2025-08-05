package it.loreluc.sagraservice.user.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import it.loreluc.sagraservice.jpa.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;

@Data @AllArgsConstructor
public class UserResponse {
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private Collection<Role> roles;
}
