package it.loreluc.sagraservice.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;

import javax.crypto.spec.SecretKeySpec;

@Configuration
public class JwtConfiguration {

    @Value("${sagra-service.jwt.key}")
    private String jwtKey;

    @Value("${spring.application.name}")
    private String issuer;

    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(jwtKey.getBytes()));
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        byte[] bytes = jwtKey.getBytes();
        final SecretKeySpec originalKey = new SecretKeySpec(bytes, 0, bytes.length,"RSA");
        final NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(originalKey)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();

        jwtDecoder.setJwtValidator(JwtValidators.createDefaultWithValidators(new JwtIssuerValidator(issuer)));

        return jwtDecoder;
    }
}