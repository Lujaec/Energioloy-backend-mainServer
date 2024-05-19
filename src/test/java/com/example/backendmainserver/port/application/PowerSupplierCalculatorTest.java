package com.example.backendmainserver.port.application;

import com.example.backendmainserver.global.application.LocalDateTimeService;
import com.example.backendmainserver.port.domain.*;
import com.example.backendmainserver.power.application.PowerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PowerSupplierCalculatorTest {

    @Mock
    private PortService portService;

    @Mock
    private PowerService powerService;

    @Spy
    private LocalDateTimeService localDateTimeService; // 실제 구현 사용

    @InjectMocks
    private PowerSupplierCalculator powerSupplierCalculator;

    @Test
    void testCalculatePowerSupplier_OptionTime() {
        // Given
        Long portId = 1L;
        Double powerUsage = 100.0;

        // Mock portService
        Port port = new Port();
        BatterySwitchOption batterySwitchOption =
                new BatterySwitchOption(BatterySwitchOptionType.OPTION_TIME,
                        "LOW-EXTERNAL,MEDIUM-BATTERY,HIGH-BATTERY");
        port.setBatterySwitchOption(batterySwitchOption);
        when(portService.getPortById(portId)).thenReturn(port);

        // When
        PowerSupplier powerSupplier = powerSupplierCalculator.calculatePowerSupplier(portId, powerUsage);

        // Then
        assertEquals(PowerSupplier.BATTERY, powerSupplier);
    }

    @Test
    void testCalculatePowerSupplier_OptionPrediction() {
        // Given
        Long portId = 1L;
        Double powerUsage = 100.0;

        // Mock portService
        Port port = new Port();
        BatterySwitchOption batterySwitchOption = new BatterySwitchOption(BatterySwitchOptionType.OPTION_PREDICTION, "80");
        port.setBatterySwitchOption(batterySwitchOption);
        when(portService.getPortById(portId)).thenReturn(port);

        // Mock powerService
        when(powerService.getPredictionPowerUsage(eq(portId), any(LocalDateTime.class))).thenReturn(120.0);

        // When
        PowerSupplier powerSupplier = powerSupplierCalculator.calculatePowerSupplier(portId, powerUsage);

        // Then
        assertEquals(PowerSupplier.BATTERY, powerSupplier);
    }

    // 추가적인 테스트 케이스들...
}