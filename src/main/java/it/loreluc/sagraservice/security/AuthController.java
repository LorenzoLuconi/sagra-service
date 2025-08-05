package it.loreluc.sagraservice.security;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.loreluc.sagraservice.error.ErrorResource;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Autenticazione")
public class AuthController {

    private final JwtAuthService jwtAuthService;

    @PostMapping("/token")
    @Operation(summary = "Autenticazione utente", description = "Autenticazione tramite username e password, ritorna struttura contenente token JWT (access_token")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Richiesta non valida")
    @ApiResponse(responseCode = "401", content = @Content, description = "Credenziali non valide")
    public AuthResponse login(@RequestBody @Valid AuthRequest authRequest) {
        return jwtAuthService.authenticate(authRequest);
    }
}