package com.example.backendmainserver.websocket.config;

import com.example.backendmainserver.websocket.application.WebSocketSessionService;
import com.example.backendmainserver.websocket.presentation.HandShakeAuthorizationInterceptor;
import com.example.backendmainserver.websocket.presentation.MessageMappingInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;


@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
@Slf4j
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {
//    private final AuthenticationInterceptor authenticationInterceptor;
    private final WebSocketSessionService webSocketSessionService;
    private final HandShakeAuthorizationInterceptor handShakeAuthorizationInterceptor;

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new MessageMappingInterceptor());
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
        // 웹소켓 endpoint 설정
        // ws://localhost:8080/raspberrypi-websocket

        registry.addEndpoint("/raspberrypi-websocket")
                .setAllowedOriginPatterns("*")
                .withSockJS()
                .setTransportHandlers()
                .setInterceptors(new HttpSessionHandshakeInterceptor() {
                    @Override
                    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
                        attributes.put("endpoint", "/raspberrypi-websocket");
                        return true;
                    }
                });

        // 웹소켓 endpoint 설정
        registry.addEndpoint("/client")
                .setAllowedOriginPatterns("*")
                .withSockJS()
                .setInterceptors(handShakeAuthorizationInterceptor);
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.addDecoratorFactory(new WebSocketHandlerDecoratorFactory() {
            @Override
            public WebSocketHandler decorate(WebSocketHandler handler) {
                return new WebSocketHandlerDecorator(handler) {
                    @Override
                    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                        Map<String, Object> attributes = session.getAttributes();
                        String endpoint = (String) attributes.get("endpoint");

                        if(endpoint.equals("/client")){
                            Long userId = Long.valueOf((Integer) attributes.get("userId"));
                            webSocketSessionService.save(session, userId);
                        }

                        super.afterConnectionEstablished(session);
                    }

                    @Override
                    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
                        log.info("WebSocket connection closed. Session ID: " + session.getId());

                        webSocketSessionService.delete(session.getId());
                        super.afterConnectionClosed(session, status);
                    }
                };
            }
        });
    }
}
