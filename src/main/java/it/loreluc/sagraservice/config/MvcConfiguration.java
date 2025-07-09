package it.loreluc.sagraservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class MvcConfiguration implements WebMvcConfigurer {

    private final CorsSettings cors;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        final CorsRegistration corsRegistration = registry.addMapping("/**");

        if (cors.getMaxAge() != null)
            corsRegistration.maxAge(cors.getMaxAge());

        if (cors.getAllowedMethods() != null)
            corsRegistration.allowedMethods(cors.getAllowedMethods());

        if (cors.getAllowedOrigins() != null)
            corsRegistration.allowedOriginPatterns(cors.getAllowedOrigins());
    }

//    @Override
//    public void addFormatters(FormatterRegistry registry) {
//        registry.removeConvertible(String.class, Collection.class);
//    }

}
