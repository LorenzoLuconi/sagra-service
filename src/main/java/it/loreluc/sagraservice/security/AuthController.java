package it.loreluc.sagraservice.security;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final JwtAuthService jwtAuthService;

    @PostMapping("/token")
    public AuthResponse login(@RequestBody AuthRequest authRequest) {
        return jwtAuthService.authenticate(authRequest);
    }
}