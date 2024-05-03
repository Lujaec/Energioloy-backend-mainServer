package com.example.backendmainserver.websocket.presentation;


import com.example.backendmainserver.PowerData.application.PowerDataService;
import com.example.backendmainserver.PowerData.domain.InMemoryPowerDataRepository;
import com.example.backendmainserver.PowerData.domain.PowerDataList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PowerDataController {
    private final PowerDataService powerDataService;
    private final InMemoryPowerDataRepository powerDataRepository;

    @MessageMapping("/data")
    @SendTo("/exe-command/data")
    public String receivePowerData(@Payload PowerDataList data) throws Exception {
        powerDataService.storeData(data);

        log.info(data.toString());

        return "from main server !! received: " + data;
    }
}
