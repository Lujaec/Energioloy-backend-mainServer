package com.example.backendmainserver.websocket.presentation;


import com.example.backendmainserver.PowerData.application.PowerDataService;
import com.example.backendmainserver.PowerData.domain.PowerDataList;
import com.example.backendmainserver.websocket.application.WebSocketSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PowerDataController {
    private final PowerDataService powerDataService;
    private final WebSocketSessionService webSocketSessionService;

    @MessageMapping("/data")
    public String receivePowerData(@Payload PowerDataList data) throws Exception {
        log.info(data.toString());

        powerDataService.storeData(data);
        webSocketSessionService.sendPowerData(data);

        return "from main server: received data = " + data;
    }
}
