package com.example.backendmainserver.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class FcmInitializer2 {

    String fcmKeyPath = "web-fcm-key.json";

    @PostConstruct
    public void initialize() {
        try {
            GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(new ClassPathResource(fcmKeyPath).getInputStream());
            FirebaseOptions options =  FirebaseOptions.builder()
                    .setCredentials(googleCredentials)
                    .build();



            FirebaseApp.initializeApp(options);
            log.info("!!!!!Fcm-web Setting Completed!!");
        } catch (IOException e) {
            log.info(">>>>>>>>FCM error");
            log.error(">>>>>>FCM error message : " + e.getMessage());
        }
    }
}