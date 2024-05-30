package com.example.backendmainserver.event.application;

import com.example.backendmainserver.event.domain.PowerSupplierChangedEvent;
import com.example.backendmainserver.history.application.PowerSupplierHistoryService;
import com.example.backendmainserver.history.application.dto.CreatePowerSupplierHistoryDto;
import com.example.backendmainserver.port.domain.Port;
import com.example.backendmainserver.port.domain.PowerSupplier;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class PowerSupplierChangedEventListener implements ApplicationListener<PowerSupplierChangedEvent> {
    private final PowerSupplierHistoryService powerSupplierHistoryService;

    @Override
    public void onApplicationEvent(PowerSupplierChangedEvent event) {
        Port port = event.getPort();
        PowerSupplier beforePowerSupplier = event.getBeforePowerSupplier();
        PowerSupplier afterPowerSupplier = event.getAfterPowerSupplier();

        CreatePowerSupplierHistoryDto createPowerSupplierHistoryDto = new CreatePowerSupplierHistoryDto(LocalDateTime.now(),
                port.getId(),
                beforePowerSupplier,
                afterPowerSupplier,
                event.getMessage());

        powerSupplierHistoryService.createHistory(createPowerSupplierHistoryDto);
    }
}