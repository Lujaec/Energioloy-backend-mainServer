package com.example.backendmainserver.fcm;

import com.example.backendmainserver.fcm.FcmMessage;
import com.example.backendmainserver.fcm.MessageDto;
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

//    // 방식1
//    public void sendMessage1(MessageDto messageDto) throws IOException {
//        String message = makeMessage(messageDto);
//
//        OkHttpClient client = new OkHttpClient();
//        RequestBody requestBody = RequestBody.create(message,
//                MediaType.get("application/json; charset=utf-8"));
//        Request request = new Request.Builder()
//                .url(API_URL)
//                .post(requestBody)
//                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
//                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
//                .build();
//
//        Response response = client.newCall(request).execute();
//
//        System.out.println(response.body().string());
//    }
//
//    private String makeMessage(MessageDto messageDto) throws JsonParseException, JsonProcessingException {
//        FcmMessage fcmMessage = FcmMessage.builder()
//                .message(FcmMessage.Message.builder()
//                        .token(messageDto.fcmToken())
//                        .notification(FcmMessage.Notification.builder()
//                                .title(messageDto.title())
//                                .body(messageDto.body())
//                                .image(null)
//                                .build()
//                        ).build()).validateOnly(false).build();
//
//        return objectMapper.writeValueAsString(fcmMessage);
//    }
//
//    private String getAccessToken() throws IOException {
//        String firebaseConfigPath = "energiology-fcm-key.json";
//
//        GoogleCredentials googleCredentials = GoogleCredentials
//                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
//                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
//
//        googleCredentials.refreshIfExpired();
//        return googleCredentials.getAccessToken().getTokenValue();
//    }

    ///////////////////////////////////////////////////////
    public void sendMessage2(MessageDto messageDto){
        //추후 userId를통해 가져오는 것으로
//        String fcmToken = "2";
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
                .putData("data1 지누", "이거뭔데지누1")
                .putData("data2 지누", "이거뭔데지누2")
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Successfully sent message: " + response);
        } catch (FirebaseMessagingException e) {
            log.error("FCM exception - "+ e.getMessage());
        }
    }

    public void sendMessageWeb()  throws ExecutionException, InterruptedException {
        String token = "ePQwOslqAV2BK3w23Tj4el:APA91bErli7i8PHWYrnoDvShlouere9iKcfwyN_B9T2n-dzY1ljEBPbVOx1IPkFQSOdY0WJdyEFqDV6B_UGCmyFfNQY2LqwZsfZxKv5bqPOnPYO2fXZJbDpOZgOaeqoSCIRNsGeQHW6G";
        Message message = Message.builder()
                .setWebpushConfig(WebpushConfig.builder()
                        .setNotification(WebpushNotification.builder()
                                .setTitle("req.getTitle()")
                                .setBody("req.getMessage()")
                                .build())
                        .build())
                .setToken(token)
                .build();



//        try {
            String response = FirebaseMessaging.getInstance().sendAsync(message).get();
//            log.info("Message sent successfully: {}", response);
//        } catch (InterruptedException e) {
//            log.error("Message sending interrupted: {}", e.getMessage());
//            Thread.currentThread().interrupt(); // Restore interrupted status
//        } catch (ExecutionException e) {
//            log.error("Message sending failed: {}", e.getCause().getMessage());
//        }

//            log.info(">>>>Send message : " + response);
    }
}