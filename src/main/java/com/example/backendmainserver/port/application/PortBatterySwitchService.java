package com.example.backendmainserver.port.application;

import com.example.backendmainserver.client.raspberry.RaspberryClient;
import com.example.backendmainserver.client.raspberry.dto.BatterySwitchRequest;
import com.example.backendmainserver.client.raspberry.dto.PortAndSupplier;
import com.example.backendmainserver.global.application.LocalDateTimeService;
import com.example.backendmainserver.port.domain.Port;
import com.example.backendmainserver.port.domain.PowerSupplier;
import com.example.backendmainserver.power.application.PowerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PortBatterySwitchService {
    private final PowerSupplierCalculator powerSupplierCalculator;
    private final PortService portService;
    private final PowerService powerService;
    private final LocalDateTimeService localDateTimeService;
    private final RaspberryClient raspberryClient;

    public void requestPortBatterySwitch(){
        List<Port> ports = portService.getAllPorts();
        List<PortAndSupplier> portAndSupplierList = new ArrayList<>();
        Map<Long, Double> powerUsageAllPorts = powerService
                .getPowerUsageAllPorts(localDateTimeService.getBeforeOneMinuteLocalDateTime(LocalDateTime.now()));

        for (Port port : ports) {
            Long portId = port.getId();
            PowerSupplier currentPowerSupplier = port.getPowerSupplier();

            PowerSupplier calculatedPowerSupplier = powerSupplierCalculator.
                    calculatePowerSupplier(portId, powerUsageAllPorts.get(portId));

            if(!currentPowerSupplier.equals(calculatedPowerSupplier))
                portAndSupplierList.add(new PortAndSupplier(portId, calculatedPowerSupplier.getName()));
        }

        raspberryClient.requestPortBatterySwitch(new BatterySwitchRequest(portAndSupplierList));
    }
}
