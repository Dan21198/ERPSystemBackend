package osu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    private static final String API_PATH_PATTERN = "/api/v1/**";
    private static final String LOCALHOST_ORIGIN = "http://localhost:3000";
    private static final String[] ALLOWED_METHODS = {"GET", "POST", "PUT", "DELETE", "OPTIONS"};
    private static final String ALLOWED_HEADERS = "*";

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping(API_PATH_PATTERN)
                        .allowedOrigins(LOCALHOST_ORIGIN)
                        .allowedMethods(ALLOWED_METHODS)
                        .allowedHeaders(ALLOWED_HEADERS)
                        .allowCredentials(true);
            }
        };
    }
}
