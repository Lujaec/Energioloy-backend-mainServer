package com.example.backendmainserver.websocket.presentation;

import com.example.backendmainserver.global.security.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandshakeInterceptor {
    private final String AUTHENTICATION_HEADER = "Authorization";
    private final String AUTHENTICATION_SCHEME = "Bearer "; // token prefix
    private final JwtProvider jwtProvider;
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        attributes.put("endpoint", "/client");
        HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();

        String accessToken = extractAccessToken(servletRequest);

        if (accessToken == null)
            return false;

        Claims claims = jwtProvider.validate(accessToken).getBody();
        attributes.put("userId", claims.get("memberId", String.class));

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }

    private String extractAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHENTICATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(AUTHENTICATION_SCHEME)) {
            return bearerToken.substring(AUTHENTICATION_SCHEME.length());  // 'Bearer ' prefix 제거
        }
        throw new PreAuthenticatedCredentialsNotFoundException("토큰이 존재하지 않습니다.");
    }
}
