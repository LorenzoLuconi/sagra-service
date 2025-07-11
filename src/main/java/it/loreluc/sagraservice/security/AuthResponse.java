package it.loreluc.sagraservice.security;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class AuthResponse {
    private String token;
    private String username;
    private Long expiresAt;
}
