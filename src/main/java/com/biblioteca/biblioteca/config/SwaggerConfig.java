package com.biblioteca.biblioteca.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API — Biblioteca Virtual")
                        .description("Sistema de gerenciamento de livros em PDF")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Biblioteca Virtual")
                                .email("contato@biblioteca.com"))
                );
    }
}