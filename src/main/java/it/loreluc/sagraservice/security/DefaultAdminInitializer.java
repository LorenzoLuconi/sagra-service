package it.loreluc.sagraservice.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DefaultAdminInitializer {

    private final CustomUserDetailsService userDetailsService;

    @Bean
    public ApplicationRunner defaultAdminRunner() {
        return args -> userDetailsService.ensureDefaultAdmin();
    }
}
