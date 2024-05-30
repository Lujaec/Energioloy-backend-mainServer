package com.example.backendmainserver.port.application;

import com.example.backendmainserver.client.raspberry.RaspberryClient;
import com.example.backendmainserver.client.raspberry.dto.request.BatterySwitchRequest;
import com.example.backendmainserver.client.raspberry.dto.request.PortAndSupplier;
import com.example.backendmainserver.client.raspberry.dto.response.BatterySwitchResponse;
import com.example.backendmainserver.client.raspberry.dto.response.PortAndResult;
import com.example.backendmainserver.event.domain.PowerSupplierChangedEvent;
import com.example.backendmainserver.global.application.LocalDateTimeService;
import com.example.backendmainserver.port.domain.*;
import com.example.backendmainserver.power.application.PowerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final ApplicationEventPublisher eventPublisher;
    private final RaspberryClient raspberryClient;

    @Scheduled(cron = "5 * * * * ?")
    public void autoPortBatterySwitch(){
        List<Port> ports = portService.getAllPorts();
        List<PortAndSupplier> portAndSupplierList = new ArrayList<>();
        Map<Long, Double> powerUsageAllPorts = powerService
                .getPowerUsageAllPorts(localDateTimeService.getBeforeOneMinuteLocalDateTime(LocalDateTime.now()));

        for (Port port : ports) {
            Long portId = port.getId();
            PowerSupplier currentPowerSupplier = port.getPowerSupplier();

            PowerSupplier calculatedPowerSupplier = powerSupplierCalculator.
                    calculatePowerSupplier(portId, powerUsageAllPorts.get(portId), currentPowerSupplier);

            if(!currentPowerSupplier.equals(calculatedPowerSupplier)) {
                portAndSupplierList.add(new PortAndSupplier(portId, calculatedPowerSupplier));
                eventPublisher.publishEvent(new PowerSupplierChangedEvent(this,
                        port,
                        currentPowerSupplier,
                        calculatedPowerSupplier,
                        createAutoSwitchMessage(port, calculatedPowerSupplier)
                ));
            }
        }

        BatterySwitchRequest batterySwitchRequest = new BatterySwitchRequest(portAndSupplierList);
        requestBatterySwitchToRaspberry(batterySwitchRequest);
    }

    private String createAutoSwitchMessage(Port port, PowerSupplier calculatedPowerSupplier){
        String message = "자동제어: ";
        BatterySwitchOptionType switchOptionType = port.getBatterySwitchOption().getBatterySwitchOptionType();
        String powerSupplierKr = calculatedPowerSupplier.getNameKr();

        if(switchOptionType.equals(BatterySwitchOptionType.OPTION_TIME)){
            String loadLevelKr = powerSupplierCalculator.determineCurrentLoadLevel().getKrName();

            message += loadLevelKr + "시 " + powerSupplierKr;
        }else if (switchOptionType.equals(BatterySwitchOptionType.OPTION_PREDICTION)){
            if (calculatedPowerSupplier.equals(PowerSupplier.EXTERNAL)){
                message += "예측 사용량 일정 비율 이상 시" + powerSupplierKr;
            }else
                message += "예측 사용량 일정 비율 미만 시" + powerSupplierKr;
        }

        return message;
    }


    @Transactional
    public void manualPortBatterySwitch(Long portId, PowerSupplier requestedPowerSupplier){
        Port port = portService.getPortById(portId);
        PowerSupplier beforePowerSupplier = port.getPowerSupplier();

        portService.updateBatterySwitchOption(portId, BatterySwitchOptionType.OPTION_MANUAL, null);
        port.setPowerSupplier(requestedPowerSupplier);

        eventPublisher.publishEvent(new PowerSupplierChangedEvent(this,
                port,
                beforePowerSupplier,
                requestedPowerSupplier,
                "수동제어"
                ));

        PortAndSupplier portAndSupplier = new PortAndSupplier(portId, requestedPowerSupplier);
        requestBatterySwitchToRaspberry(new BatterySwitchRequest(List.of(portAndSupplier)));
    }

    public void requestBatterySwitchToRaspberry(BatterySwitchRequest batterySwitchRequest) {
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
                    map.put(e.portId(), e.powerSupplier().name());
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

    public void updateAutoSwitchConfig(Long portId, BatterySwitchOption batterySwitchOption){
        portService.updateBatterySwitchOption(portId,
                batterySwitchOption.getBatterySwitchOptionType(),
                batterySwitchOption.getOptionConfiguration());
    }
}
