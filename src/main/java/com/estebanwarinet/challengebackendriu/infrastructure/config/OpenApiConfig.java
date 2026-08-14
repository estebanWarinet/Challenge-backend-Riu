package com.estebanwarinet.challengebackendriu.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI().info(new Info()
                .title("RIU Hotel Search API")
                .description("API para registrar búsquedas de disponibilidad y contar búsquedas idénticas")
                .version("1.0"));
    }
}
