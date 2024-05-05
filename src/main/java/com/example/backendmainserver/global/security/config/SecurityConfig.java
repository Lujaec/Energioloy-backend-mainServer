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

import java.util.Collections;
import java.util.List;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;
import static org.springframework.web.servlet.function.RequestPredicates.headers;


@Configuration
@EnableWebSecurity
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

        http
//                .securityMatchers(matcher -> matcher
//                        .requestMatchers(authorizeRequestMatchers())) // CORS 문제 생김
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(authorizeRequestMatchers()).authenticated() // 인증된 사용자만 허용
                        .requestMatchers(permitAllRequestMatchers()).permitAll() // 인증 필요 없는 경로
                        .anyRequest().permitAll())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // jwt 인증/인가 필터 추가
                .addFilterBefore(
                        new JwtAuthenticationFilter(new ProviderManager(jwtAuthenticationProvider)),
                        UsernamePasswordAuthenticationFilter.class)
                // jwt 인증/인가 필터에서 발생한 에러 handle 필터 추가
                .addFilterBefore(new JwtAuthenticationHandlerFilter(), JwtAuthenticationFilter.class)
                .exceptionHandling(handler -> handler.accessDeniedHandler(accessDeniedHandler))
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));

        return http.build();
    }

    /**
     * jwt 인증, 인가 적용 x filterChain
     */
    @Bean
    @Order(2) // 우선 순위 2
    public SecurityFilterChain permitAllFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(permitAllRequestMatchers()).permitAll()
                        .anyRequest().denyAll()
                ).csrf(AbstractHttpConfigurer::disable)
        ;
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
    private RequestMatcher[] authorizeRequestMatchers() {
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
                antMatcher(POST, "/api/user"),
                antMatcher(GET, "/api/health"),
                antMatcher(GET, "/client/**"),
                antMatcher(POST, "/client/**")
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

    //    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOriginPatterns(List.of("http://localhost:3000")); // React 앱의 URL
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
//        configuration.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedHeaders(Collections.singletonList("*"));
            config.setAllowedMethods(Collections.singletonList("*"));
            config.setAllowedOriginPatterns(Collections.singletonList("*")); // 허용할 origin
            config.setAllowCredentials(true);
            return config;
        };
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


