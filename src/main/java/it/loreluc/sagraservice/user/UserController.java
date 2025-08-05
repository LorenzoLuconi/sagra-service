package it.loreluc.sagraservice.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.loreluc.sagraservice.error.ErrorResource;
import it.loreluc.sagraservice.error.SagraAuthorizationException;
import it.loreluc.sagraservice.jpa.User;
import it.loreluc.sagraservice.user.resource.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Utenti")
public class UserController {

    private final UsersRepository usersRepository;

    @GetMapping("/v1/user")
    @Operation(summary = "Dati utente autenticato")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Richiesta non valida")
    @ApiResponse(responseCode = "401", content = @Content)
    @ApiResponse(responseCode = "403", content = @Content)
    public UserResponse authenticatedUser() {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        final User user = usersRepository.findByUsername(authentication.getName()).orElseThrow(SagraAuthorizationException::new);

        return new UserResponse(user.getUsername(), List.of(user.getRole()));
    }

}
