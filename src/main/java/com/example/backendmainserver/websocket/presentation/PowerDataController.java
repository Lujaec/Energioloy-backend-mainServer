package com.example.backendmainserver.websocket.presentation;


import com.example.backendmainserver.websocket.presentation.dto.PowerDataList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class PowerDataController {


    @MessageMapping("/data")
    @SendTo("/exe-command/data")
    public String receivePowerData(@Payload PowerDataList data) throws Exception {
        Thread.sleep(1000); // simulated delay
        log.info(data.toString());
        return "from main server !! received: " + data;
    }

}
