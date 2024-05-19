package com.example.backendmainserver.client.raspberry;

import com.example.backendmainserver.client.raspberry.dto.BatterySwitchRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="raspberry-service", url = "http://localhost:8081")
public interface RaspberryClient {
    @PostMapping("/api/ports/battery-switch")
    void requestPortBatterySwitch(@RequestBody BatterySwitchRequest batterySwitchRequest);
}

