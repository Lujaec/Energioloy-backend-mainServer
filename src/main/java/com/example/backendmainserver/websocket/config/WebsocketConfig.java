package com.example.backendmainserver.websocket.config;

import com.example.backendmainserver.websocket.presentation.AuthenticationInterceptor;
import com.example.backendmainserver.websocket.presentation.MesesageMappingInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;


@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
@Slf4j
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new MesesageMappingInterceptor());
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        //메시지 브로커 활성화 및 클라이언트가 구독할 수있는 주제 지정
        config.enableSimpleBroker("/exe-command");
        //클라이언트에서 서버로 메시지를 보낼 때 사용할 경로의 접두사를 설정
        config.setApplicationDestinationPrefixes("/main-server");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // stomp 웹소켓 endpoint 설정
        // ws://localhost:8080/raspberrypi-websocket
        // http://배포주소:8080/raspberrypi-websocket

        registry.addEndpoint("/raspberrypi-websocket")
                .setAllowedOrigins("*")
                .withSockJS()
                .setInterceptors(new HttpSessionHandshakeInterceptor() {
                    @Override
                    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
                        attributes.put("endpoint", "/raspberrypi-websocket");
                        return true;
                    }
                });

        // stomp 웹소켓 endpoint 설정
        // ws://localhost:8080/client
        registry.addEndpoint("/client")
                .setAllowedOrigins("*")
                .withSockJS()
                .setInterceptors(new AuthenticationInterceptor());
    }
}
