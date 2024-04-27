package com.example.backendmainserver.websocket.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@EnableWebSocketMessageBroker
@Slf4j
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        //메시지 브로커 활성화 및 클라이언트가 구독할 수있는 주제 지정
        config.enableSimpleBroker("/exe-command");
        //클라이언트에서 서버로 메시지를 보낼 때 사용할 경로의 접두사를 설정
        config.setApplicationDestinationPrefixes("/raspberrypi");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // stomp 웹소켓 endpoint 설정
        // ws://localhost:8080/raspberrypi-websocket
        // http://배포주소:8080/raspberrypi-websocket

        registry.addEndpoint("/raspberrypi-websocket")
                .setAllowedOrigins("*")
                .withSockJS();
    }
}
