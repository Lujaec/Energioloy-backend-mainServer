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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
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

        BatterySwitchRequest batterySwitchRequest = new BatterySwitchRequest(portAndSupplierList);
        BatterySwitchResponse batterySwitchResponse =
                raspberryClient.requestPortBatterySwitch(batterySwitchRequest);

        updatePowerSupplier(batterySwitchRequest, batterySwitchResponse);
    }

    private void updatePowerSupplier(BatterySwitchRequest batterySwitchRequest,
                                     BatterySwitchResponse batterySwitchResponse){
        List<PortAndSupplier> portAndSuppliers = batterySwitchRequest.portAndSuppliers();
        List<PortAndResult> portAndResults = batterySwitchResponse.portAndResults();

        Map<Long, String> map = new HashMap();

        portAndSuppliers.stream().forEach(
                (e)->{
                    map.put(e.portId(), e.powerSupplier());
                }
        );

        for (PortAndResult portAndResult : portAndResults) {
            Long portId = portAndResult.portId();
            String result = portAndResult.result();

            if(result.equals("success")){
                String nextSupplier = map.get(portId);
                portService.updatePowerSupplier(portId, PowerSupplier.valueOf(nextSupplier));
            }
        }
    }
}
