package com.example.backendmainserver.global.security.config;

import com.example.backendmainserver.global.security.exception.AccessDeniedHandlerImpl;
import com.example.backendmainserver.global.security.filter.JwtAuthenticationFilter;
import com.example.backendmainserver.global.security.filter.JwtAuthenticationHandlerFilter;
import com.example.backendmainserver.global.security.provider.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.List;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;


@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final AccessDeniedHandlerImpl accessDeniedHandler;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    /**
     * jwt 인증, 인가 적용 o filterChain
     */
    @Bean
    @Order(1) // 우선 순위 1
    public SecurityFilterChain authorizeFilterChain(HttpSecurity http) throws Exception {
        httpSecuirtySetting(http);

        http
                .securityMatchers(matcher -> matcher
                        .requestMatchers(authorizeRequestMathcers()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(authorizeRequestMathcers())
                        .authenticated() // 인증된 사용자만 허용
//                            .hasAuthority(RolesType.ROLE_USER.name()) // 빌딩 관리자 - 사용자가 ROLE구분이 아닌
                        .anyRequest().denyAll())
                // jwt 인증/인가 필터 추가
                .addFilterBefore(
                        new JwtAuthenticationFilter(new ProviderManager(jwtAuthenticationProvider)),
                        UsernamePasswordAuthenticationFilter.class)
                // jwt 인증/인가 필터에서 발생한 에러 handle 필터 추가
                .addFilterBefore(new JwtAuthenticationHandlerFilter(), JwtAuthenticationFilter.class)
                .exceptionHandling(handler -> handler.accessDeniedHandler(accessDeniedHandler));

        return http.build();
    }

    /**
     * jwt 인증, 인가 적용 x filterChain
     */
    @Bean
    @Order(2) // 우선 순위 2
    public SecurityFilterChain permitAllFilterChain(HttpSecurity http) throws Exception {
        httpSecuirtySetting(http);

        http
                .securityMatchers(matcher -> matcher
                        .requestMatchers(permitAllRequestMatchers()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(permitAllRequestMatchers()).permitAll()
                        .anyRequest().denyAll()
                );
        return http.build();
    }

    /**
     * security filter 적용 x
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(swaggerRequestMatchers());
    }

    /**
     * authorize endpoint >> 인증/인가 필요한 endpoint
     */
    private RequestMatcher[] authorizeRequestMathcers() {
        List<RequestMatcher> requestMatchers = List.of(
                antMatcher(POST, "/member/refresh")
        );
        return requestMatchers.toArray(RequestMatcher[]::new);
    }

    /**
     * permitAll endpoint >> 인증/인가 필요 x endpoint
     */
    private RequestMatcher[] permitAllRequestMatchers() {
        List<RequestMatcher> requestMatchers = List.of(
                antMatcher(POST, "/api/user/login"),
                antMatcher(POST, "/api/user")
        );
        return requestMatchers.toArray(RequestMatcher[]::new);
    }

    /**
     * swagger-ui와 관련된 endpoint >> security filter 적용 x
     */
    private RequestMatcher[] swaggerRequestMatchers() {
        List<RequestMatcher> requestMatchers = List.of(
                antMatcher(GET, "/swagger-ui/**"),
                antMatcher(GET, "/v3/api-docs/**")
        );
        return requestMatchers.toArray(RequestMatcher[]::new);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of("http://localhost:8080")
        );

        configuration.setAllowedMethods(
                List.of("GET", "POST", "PUT", "PATCH", "DELETE")
        );

        configuration.addAllowedHeader("*"); // 모든 헤더 허용
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;

    }

    private void httpSecuirtySetting(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable) //jwt를 사용함으로 csrf 비활성
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // cors 정책 반영
                .sessionManagement(
                        session -> session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS)) // jwt를 사용함으로 세션 비활성
                .formLogin(AbstractHttpConfigurer::disable) // form-login 비활성
                .httpBasic(AbstractHttpConfigurer::disable) // basic Authentication 비활성
                .anonymous(AbstractHttpConfigurer::disable); // 익명 사용자 접근 비활성, 모든 요청 인증 필요
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


