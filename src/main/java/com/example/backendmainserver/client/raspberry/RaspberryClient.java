package com.example.backendmainserver.client.raspberry;

import com.example.backendmainserver.client.raspberry.dto.request.BatterySwitchRequest;
import com.example.backendmainserver.client.raspberry.dto.response.BatterySwitchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="raspberry-service", url = "http://localhost:8081")
public interface RaspberryClient {
    @PostMapping("/api/ports/battery-switch")
    BatterySwitchResponse requestPortBatterySwitch(@RequestBody BatterySwitchRequest batterySwitchRequest);
}

