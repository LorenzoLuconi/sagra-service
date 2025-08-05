package it.loreluc.sagraservice.security;

import it.loreluc.sagraservice.config.SagraSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final JwtEncoder encoder;
    private final JwtDecoder decoder;

    private final SagraSettings sagraSettings;
    @Value("${spring.application.name}")
    private String issuer;


    public String generateToken(Authentication authentication) {
        final Instant now = Instant.now();

        final JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(now.plus(sagraSettings.getTokenExpire(), ChronoUnit.MINUTES))
                .subject(authentication.getName())
                .claim("authorities", authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .build();

        final JwtEncoderParameters encoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
        return this.encoder.encode(encoderParameters).getTokenValue();
    }

    public Long extractExpirationTime(String token) {
        final Jwt jwt = decoder.decode(token);
        final Instant exp = (Instant) jwt.getClaim("exp");
        return exp.toEpochMilli();
    }
}