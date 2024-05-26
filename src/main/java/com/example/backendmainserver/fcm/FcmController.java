package com.example.backendmainserver.fcm;

import com.example.backendmainserver.global.response.SuccessResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Tag(name = "FCM API", description = "FCM 메세지를 보내는 api 컨트롤러")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/fcm")
public class FcmController {
    private final FcmService fcmService;

    @SecurityRequirements(value = {})
    @PostMapping("/message")
    public ResponseEntity<SuccessResponse<String>> sendMessage(@RequestBody MessageDto messageDto) throws IOException {
        log.info("msg fcm token : "+messageDto.fcmToken());
        log.info("msg title : "+messageDto.title());
        log.info("msg body : "+messageDto.body());

        fcmService.sendMessage(
                messageDto);

        return SuccessResponse.of("메세지 발송 성공");

    }

}