package com.example.backendmainserver.websocket.presentation;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MesesageMappingInterceptor implements ChannelInterceptor {
    Map<String, List<String>> messageMapper;

    public MesesageMappingInterceptor() {
        this.messageMapper = new ConcurrentHashMap<>();

        messageMapper.put("/raspberrypi-websocket", List.of("/main-server/data"));
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String simpDestination = accessor.getDestination();

        if((((StompCommand) accessor.getHeader("stompCommand")).name()).equals("SEND")) {
            String connectionEndpoint = (String) accessor.getSessionAttributes().get("endpoint");
            List<String> messageEndpoints = messageMapper.get(connectionEndpoint);
            // 특정 엔드포인트를 통해 온 메시지인지 확인
            if ("/raspberrypi-websocket".equals(connectionEndpoint) && messageEndpoints != null && messageEndpoints.contains(simpDestination)) {
                return message;
            }

            throw new IllegalArgumentException();
        }

        return message;
    }
}
