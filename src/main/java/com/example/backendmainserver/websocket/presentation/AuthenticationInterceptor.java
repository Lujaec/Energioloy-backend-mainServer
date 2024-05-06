package com.example.backendmainserver.websocket.presentation;

import com.example.backendmainserver.global.security.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;


@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements ChannelInterceptor {
    private final String AUTHENTICATION_HEADER = "Authorization";
    private final String AUTHENTICATION_SCHEME = "Bearer "; // token prefix
    private final JwtProvider jwtProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        assert headerAccessor != null;
        if (headerAccessor.getCommand() == StompCommand.CONNECT) { // 연결 시에한 header 확인
            String accessToken = String.valueOf(headerAccessor.getNativeHeader("Authorization").get(0));

            if (StringUtils.hasText(accessToken) && accessToken.startsWith(AUTHENTICATION_SCHEME)) {
                accessToken = accessToken.substring(AUTHENTICATION_SCHEME.length());  // 'Bearer ' prefix 제거
            }

            try {
                Claims claims = jwtProvider.validate(accessToken).getBody();
                headerAccessor.addNativeHeader("User", claims.get("memberId", String.class));
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        return message;
    }

//    @Override
//    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
//        attributes.put("endpoint", "/client");
//        HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
//
//        String accessToken = extractAccessToken(servletRequest);
//
//        if (accessToken == null)
//            return false;
//
//        Claims claims = jwtProvider.validate(accessToken).getBody();
//        attributes.put("userId", claims.get("memberId", String.class));
//
//        return true;
//    }
//
//    @Override
//    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
//
//    }

    private String extractAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHENTICATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(AUTHENTICATION_SCHEME)) {
            return bearerToken.substring(AUTHENTICATION_SCHEME.length());  // 'Bearer ' prefix 제거
        }

        throw new IllegalArgumentException("토큰이 존재하지 않습니다.");
    }
}