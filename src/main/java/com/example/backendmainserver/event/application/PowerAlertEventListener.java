package com.example.backendmainserver.event.application;

import com.example.backendmainserver.event.domain.PowerAlertEvent;
import com.example.backendmainserver.event.domain.PowerSupplierChangedEvent;
import com.example.backendmainserver.history.application.PowerSupplierHistoryService;
import com.example.backendmainserver.history.application.dto.CreatePowerSupplierHistoryDto;
import com.example.backendmainserver.notification.application.PowerAlertService;
import com.example.backendmainserver.notification.application.dto.PowerAlertDto;
import com.example.backendmainserver.port.domain.Port;
import com.example.backendmainserver.port.domain.PowerSupplier;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class PowerAlertEventListener implements ApplicationListener<PowerAlertEvent> {
    private final PowerAlertService powerAlertService;

    @Override
    public void onApplicationEvent(PowerAlertEvent event) {
        Long roomId = event.getRoomId();
        Long portId = event.getPortId();
        String fcmToken = event.getFcmToken();
        Double powerUsage = event.getPowerUsage();

        PowerAlertDto powerAlertDto = new PowerAlertDto(roomId, portId, fcmToken, powerUsage);
        powerAlertService.alert(powerAlertDto);
    }
}