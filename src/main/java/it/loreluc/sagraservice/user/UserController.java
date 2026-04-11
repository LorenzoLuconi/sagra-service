package it.loreluc.sagraservice.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.loreluc.sagraservice.error.ErrorResource;
import it.loreluc.sagraservice.jpa.User;
import it.loreluc.sagraservice.security.CustomUserDetailsService;
import it.loreluc.sagraservice.user.resource.UserRequest;
import it.loreluc.sagraservice.user.resource.UserResponse;
import it.loreluc.sagraservice.user.resource.UserUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Tag(name = "Utenti")
public class UserController {

    private final CustomUserDetailsService userDetailsService;

    @GetMapping
    @Operation(summary = "Lista utenti")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "401", content = @Content)
    @ApiResponse(responseCode = "403", content = @Content)
    public List<UserResponse> listUsers() {
        return userDetailsService.findAllUsers().stream().map(this::toResponse).toList();
    }

    @GetMapping("/{username}")
    @Operation(summary = "Utente tramite username")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "401", content = @Content)
    @ApiResponse(responseCode = "403", content = @Content)
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Utente non trovato")
    public UserResponse getUser(@PathVariable String username) {
        return toResponse(userDetailsService.findUser(username));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crea un utente")
    @ApiResponse(responseCode = "201")
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Richiesta non valida")
    @ApiResponse(responseCode = "401", content = @Content)
    @ApiResponse(responseCode = "403", content = @Content)
    @ApiResponse(responseCode = "409", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Utente già esistente")
    public UserResponse createUser(@RequestBody @Valid UserRequest request) {
        return toResponse(userDetailsService.createUser(
                request.getUsername(),
                request.getName(),
                request.getRole(),
                request.getPassword()
        ));
    }

    @PutMapping("/{username}")
    @Operation(summary = "Modifica un utente")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Richiesta non valida")
    @ApiResponse(responseCode = "401", content = @Content)
    @ApiResponse(responseCode = "403", content = @Content)
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Utente non trovato")
    @ApiResponse(responseCode = "409", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Operazione non consentita sull'ultimo admin o password invariata")
    public UserResponse updateUser(@PathVariable String username, @RequestBody @Valid UserUpdateRequest request) {
        return toResponse(userDetailsService.updateUser(
                username,
                request.getName(),
                request.getRole(),
                request.getPassword()
        ));
    }

    @DeleteMapping("/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Cancella un utente")
    @ApiResponse(responseCode = "204")
    @ApiResponse(responseCode = "401", content = @Content)
    @ApiResponse(responseCode = "403", content = @Content)
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Utente non trovato")
    @ApiResponse(responseCode = "409", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Operazione non consentita su se stessi o sull'ultimo admin")
    public void deleteUser(@PathVariable String username, Authentication authentication) {
        userDetailsService.deleteUser(username, authentication.getName());
    }

    private UserResponse toResponse(User user) {
        return UserResponse.builder()
                .username(user.getUsername())
                .name(user.getName())
                .role(user.getRole())
                .build();
    }
}
