package com.example.backendmainserver.websocket.presentation;

import com.example.backendmainserver.global.security.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class HandShakeAuthorizationInterceptor implements HandshakeInterceptor {
    private final String AUTHENTICATION_HEADER = "Authorization";
    private final String AUTHENTICATION_SCHEME = "Bearer "; // token prefix
    private final JwtProvider jwtProvider;
    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        attributes.put("endpoint", "/client");
        String accessToken = extractAccessToken(request);

        if (accessToken == null)
            return false;

        Claims claims = jwtProvider.validate(accessToken).getBody();
        attributes.put("userId", claims.get("memberId", Integer.class));

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }

    private String extractAccessToken(ServerHttpRequest request) {
        MultiValueMap<String, String> parameters = UriComponentsBuilder.fromUri(request.getURI()).build().getQueryParams();
        return parameters.getFirst("token");
    }
}
