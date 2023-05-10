package com.pentspace.accountmgtservice.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    public final String [] ALLOWED_ORIGINS = {
            "https://localhost:30303",
            "https://localhost:30304",
            "https://localhost:30305",
            "https://localhost:30306",
            "https://localhost:30307"

    };


    public WebMvcConfigurer corsConfigurer() {

        return new WebMvcConfigurer() {
                public void addCorsMapping(CorsRegistry corsRegistry) {
                    corsRegistry.addMapping("/**")
                            .allowedMethods("*")
                            .allowedOrigins(ALLOWED_ORIGINS);
                }
        };
    }
}
