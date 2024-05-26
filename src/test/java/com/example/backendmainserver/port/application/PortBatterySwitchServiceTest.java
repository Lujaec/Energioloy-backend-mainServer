package com.example.backendmainserver.port.application;

import com.example.backendmainserver.client.raspberry.RaspberryClient;
import com.example.backendmainserver.client.raspberry.dto.request.BatterySwitchRequest;
import com.example.backendmainserver.client.raspberry.dto.request.PortAndSupplier;
import com.example.backendmainserver.client.raspberry.dto.response.BatterySwitchResponse;
import com.example.backendmainserver.client.raspberry.dto.response.PortAndResult;
import com.example.backendmainserver.global.application.LocalDateTimeService;
import com.example.backendmainserver.port.domain.Port;
import com.example.backendmainserver.port.domain.PowerSupplier;
import com.example.backendmainserver.power.application.PowerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PortBatterySwitchServiceTest {
    @Mock
    private PowerSupplierCalculator powerSupplierCalculator;

    @Mock
    private PortService portService;

    @Mock
    private PowerService powerService;

    @Spy
    private LocalDateTimeService localDateTimeService;

    @Mock
    private RaspberryClient raspberryClient;

    @InjectMocks
    private PortBatterySwitchService portBatterySwitchService;
    @Test
    void testRequestPortBatterySwitchData() {
        // Setting up Ports
        Port port1 = Port.builder().id(1L).powerSupplier(PowerSupplier.BATTERY).build();
        Port port2 = Port.builder().id(2L).powerSupplier(PowerSupplier.EXTERNAL).build();
        Port port3 = Port.builder().id(3L).powerSupplier(PowerSupplier.EXTERNAL).build();
        List<Port> ports = List.of(port1, port2, port3);
        BatterySwitchResponse mockResponse = new BatterySwitchResponse(List.of(
                new PortAndResult(port2.getId(), "success"),
                new PortAndResult(port3.getId(), "success")));

        when(portService.getAllPorts()).thenReturn(ports);

        // Setting up power usage and power suppliers
        Map<Long, Double> powerUsageAllPorts = new HashMap<>();
        powerUsageAllPorts.put(1L, 100.0);
        powerUsageAllPorts.put(2L, 200.0);
        powerUsageAllPorts.put(3L, 200.0);

        when(powerService.getPowerUsageAllPorts(any(LocalDateTime.class))).thenReturn(powerUsageAllPorts);
        when(powerSupplierCalculator.calculatePowerSupplier(1L, 100.0, PowerSupplier.BATTERY)).thenReturn(PowerSupplier.BATTERY);

        when(powerSupplierCalculator.calculatePowerSupplier(2L, 200.0, PowerSupplier.EXTERNAL)).thenReturn(PowerSupplier.BATTERY);

        when(powerSupplierCalculator.calculatePowerSupplier(3L, 200.0, PowerSupplier.EXTERNAL)).thenReturn(PowerSupplier.BATTERY);

        when(raspberryClient.requestPortBatterySwitch(any(BatterySwitchRequest.class))).thenReturn(mockResponse);


        // Then
        portBatterySwitchService.autoPortBatterySwitch();

        // Verify if the request to switch the battery includes the right ports
        ArgumentCaptor<BatterySwitchRequest> batterySwitchRequestArgumentCaptor = ArgumentCaptor.forClass(BatterySwitchRequest.class);
        verify(raspberryClient).requestPortBatterySwitch(batterySwitchRequestArgumentCaptor.capture());
        BatterySwitchRequest batterySwitchRequest = batterySwitchRequestArgumentCaptor.getValue();

        List<PortAndSupplier> portAndSuppliers = batterySwitchRequest.portAndSuppliers();
        assertEquals(2, portAndSuppliers.size());

        assertEquals(2L, portAndSuppliers.get(0).portId());
        assertEquals("BATTERY", portAndSuppliers.get(0).powerSupplier().getName());

        assertEquals(3L, portAndSuppliers.get(1).portId());
        assertEquals("BATTERY", portAndSuppliers.get(0).powerSupplier().getName());
    }
}
