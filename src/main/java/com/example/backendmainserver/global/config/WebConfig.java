package com.example.backendmainserver.global.config;

import com.example.backendmainserver.auth.presentation.AdminAuthenticationPrincipalArgumentResolver;
import com.example.backendmainserver.auth.presentation.AuthenticationPrincipalArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;


@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver;
    private final AdminAuthenticationPrincipalArgumentResolver adminAuthenticationPrincipalArgumentResolver;

    public WebConfig(AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver,
                     AdminAuthenticationPrincipalArgumentResolver adminAuthenticationPrincipalArgumentResolver) {
        this.authenticationPrincipalArgumentResolver = authenticationPrincipalArgumentResolver;
        this.adminAuthenticationPrincipalArgumentResolver = adminAuthenticationPrincipalArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(authenticationPrincipalArgumentResolver);
        argumentResolvers.add(adminAuthenticationPrincipalArgumentResolver);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                .allowedHeaders("Authorization", "Content-Type")
                .exposedHeaders("Custom-Header")
                .maxAge(3600);
    }
}

