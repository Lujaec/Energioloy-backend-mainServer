package com.example.backendmainserver.fcm;

import com.example.backendmainserver.fcm.FcmMessage;
import com.example.backendmainserver.fcm.MessageDto;
import com.example.backendmainserver.global.exception.GlobalException;
import com.example.backendmainserver.global.response.ErrorCode;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.net.HttpHeaders;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmService {
    private final String API_URL = "https://fcm.googleapis.com/v1/projects/" +
            "energiology-4bacb/messages:send";
    private final ObjectMapper objectMapper;

    ///////////////////////////////////////////////////////
    public void sendMessage(MessageDto messageDto){
        String fcmToken = messageDto.fcmToken();

        //알림 메시지에 들어 갈 noti
        Notification notification = Notification.builder()
                .setTitle(messageDto.title())
                .setBody(messageDto.body())
                .build();

        //메세지
        Message message = Message.builder()
                .setToken(fcmToken)
                .setNotification(notification)

                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Successfully sent message: " + response);
        } catch (FirebaseMessagingException e) {
            log.error("FCM exception - "+ e.getMessage());
            throw new GlobalException(ErrorCode.FCM_TOKEN_NOT_FOUND);
        }
    }

}