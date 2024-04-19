package com.example.backendmainserver.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "Energiology Main Server API 명세서",
                description = "Energiology Main Server API 명세서",
                version = "v1"))
@Configuration
public class SwaggerConfig {
}