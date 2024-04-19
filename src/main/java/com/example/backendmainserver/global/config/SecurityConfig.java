package com.example.backendmainserver.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;


@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
//    private final UserDetailService userService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
//        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
        http
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers("/**").permitAll()
                                .anyRequest().denyAll())
                .formLogin(AbstractHttpConfigurer::disable); //  폼 기반 시큐리티 로그인 비활성화
        //

//
//                .httpBasic(httpBasic -> httpBasic.disable())
//                .csrf(csrf -> csrf.disable())
//                .cors(Customizer.withDefaults())
//                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(request ->
//                        request.requestMatchers(mvcMatcherBuilder.pattern("/auth/login")).permitAll()
//                                .requestMatchers(mvcMatcherBuilder.pattern("/css/**")).permitAll()
//                                .requestMatchers(mvcMatcherBuilder.pattern("/js/**")).permitAll()
//                                .requestMatchers(mvcMatcherBuilder.pattern("/images/**")).permitAll()
//                                .requestMatchers(mvcMatcherBuilder.pattern("/error")).permitAll()
//                                .requestMatchers(mvcMatcherBuilder.pattern("/favicon.ico")).permitAll()
//                                .requestMatchers(mvcMatcherBuilder.pattern("/swagger-ui/**")).permitAll()
//                                .requestMatchers(mvcMatcherBuilder.pattern("/swagger-resources/**")).permitAll()
//                                .requestMatchers(mvcMatcherBuilder.pattern("/v3/api-docs/**")).permitAll()
////                                .requestMatchers(mvcMatcherBuilder.pattern("/auth/admin/**")).hasAuthority(UserRole.ADMIN.name())
//                                .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.GET,"/api/**")).permitAll()
//                                .anyRequest().authenticated());
//                .addFilterBefore(new JwtTokenFilter(userService, secretKey), UsernamePasswordAuthenticationFilter.class)

        return http.build();
    }
//
//    // 1. 스프링 시큐리티 비활성화
//    @Bean
//    public WebSecurityCustomizer configure() {
//        return (web) -> web.ignoring()
//                .requestMatchers(toH2Console())
//                .requestMatchers("/static/**");
//    }

}

