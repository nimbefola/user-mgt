package com.pentspace.accountmgtservice.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    public final String [] ALLOWED_ORIGINS = {
            "http://localhost:30303",
            "http://localhost:30304",
            "http://localhost:30305",
            "http://localhost:30306",
            "http://localhost:30307"

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
