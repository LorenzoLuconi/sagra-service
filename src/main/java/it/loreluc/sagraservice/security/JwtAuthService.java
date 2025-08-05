package it.loreluc.sagraservice.security;

import it.loreluc.sagraservice.config.SagraSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class JwtAuthService {

    private final AuthenticationManager authenticationManager;

    private final SagraSettings sagraSettings;
    private final JwtEncoder encoder;
    private final JwtDecoder decoder;

    @Value("${spring.application.name}")
    private String issuer;


    public AuthResponse authenticate(AuthRequest authRequest) {
        var token = new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword());
        final Authentication authentication = authenticationManager.authenticate(token);

        final Instant expiresAt = Instant.now().plus(sagraSettings.getTokenExpire(), ChronoUnit.MINUTES);
        final String jwtToken = generateToken(authentication, expiresAt);

        return new AuthResponse(jwtToken, ChronoUnit.SECONDS.between(Instant.now(), expiresAt));
    }



    private String generateToken(Authentication authentication, Instant expiresAt ) {
        final Instant now = Instant.now();

        final JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(expiresAt)
                .subject(authentication.getName())
                .claim("roles", authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .filter( a -> a.startsWith("ROLE_"))
                        .map( a -> a.replaceFirst("ROLE_", "").toLowerCase()).toList()
                )
                .build();

        final JwtEncoderParameters encoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
        return this.encoder.encode(encoderParameters).getTokenValue();
    }
}