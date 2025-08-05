package it.loreluc.sagraservice.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data @RequiredArgsConstructor
public class AuthResponse {
    @JsonProperty("access_token")
    private final String accessToken;

    @JsonProperty("expires_in")
    private final Long expiresIn;

    @JsonProperty("token_type")
    private final String tokenType = "Bearer";
}
