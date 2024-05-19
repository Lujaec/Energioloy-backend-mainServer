package com.example.backendmainserver.port.application;

import com.example.backendmainserver.port.domain.*;
import com.example.backendmainserver.power.application.PowerService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class PowerSupplierCalculatorTest {

    @Mock
    private PortService portService;

    @Mock
    private PowerService powerService;

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
        LocalDateTime now = LocalDateTime.now();
        when(powerService.getPredictionPowerUsage(portId, now)).thenReturn(120.0);

        // When
        PowerSupplier powerSupplier = powerSupplierCalculator.calculatePowerSupplier(portId, powerUsage);

        // Then
        assertEquals(PowerSupplier.BATTERY, powerSupplier);
    }

    // 추가적인 테스트 케이스들...
}