package com.example.webbprojekt1.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("WebbProjekt1 API")
                        .version("1.0.0")
                        .description("API Documentation for WebbProjekt1")
                        .contact(new Contact()
                                .name("Developer")
                                .url("https://example.com")));
    }
}

