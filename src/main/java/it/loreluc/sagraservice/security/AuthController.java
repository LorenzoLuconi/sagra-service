package it.loreluc.sagraservice.security;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.loreluc.sagraservice.error.ErrorResource;
import it.loreluc.sagraservice.jpa.User;
import it.loreluc.sagraservice.security.resource.AuthenticatedUserResponse;
import it.loreluc.sagraservice.security.resource.ChangePasswordRequest;
import it.loreluc.sagraservice.security.resource.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticazione")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final HttpSessionSecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
    private final SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

    @PostMapping("/login")
    @Operation(summary = "Effettua il login utente")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Richiesta non valida")
    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Credenziali non valide")
    public AuthenticatedUserResponse login(@RequestBody @Valid LoginRequest loginRequest,
                                           HttpServletRequest request,
                                           HttpServletResponse response) {
        final Authentication authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.getUsername(), loginRequest.getPassword())
        );

        request.getSession(true);
        request.changeSessionId();
        final SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        securityContextRepository.saveContext(context, request, response);

        return toResponse((User) authentication.getPrincipal());
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Effettua il logout utente")
    @ApiResponse(responseCode = "204")
    @ApiResponse(responseCode = "401", content = @Content)
    public void logout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        logoutHandler.logout(request, response, authentication);
    }

    @GetMapping("/me")
    @Operation(summary = "Restituisce l'utente autenticato")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "401", content = @Content)
    public AuthenticatedUserResponse me(Authentication authentication) {
        return toResponse(userDetailsService.findUser(authentication.getName()));
    }

    @PostMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Cambia la password dell'utente autenticato")
    @ApiResponse(responseCode = "204")
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Richiesta non valida")
    @ApiResponse(responseCode = "401", content = @Content)
    @ApiResponse(responseCode = "409", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Nuova password identica alla precedente")
    public void changePassword(Authentication authentication, @RequestBody @Valid ChangePasswordRequest request) {
        userDetailsService.changePassword(authentication.getName(), request.getCurrentPassword(), request.getNewPassword());
    }

    private AuthenticatedUserResponse toResponse(User user) {
        return AuthenticatedUserResponse.builder()
                .username(user.getUsername())
                .name(user.getName())
                .role(user.getRole())
                .build();
    }
}
