package com.example.backendmainserver.port.application;

import com.example.backendmainserver.global.application.LocalDateTimeService;
import com.example.backendmainserver.port.domain.*;
import com.example.backendmainserver.power.application.PowerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PowerSupplierCalculator {
    private final PortService portService;
    private final PowerService powerService;
    private final LocalDateTimeService localDateTimeService;

    //현재 시점에 적합한 PowerSupplier를 계산
    //포트에 지정된 OptionType이 Manual일 경우 현재 자동 스위칭은 비활성화 되므로, 현재의 powerSupplier를 그대로 반환
    public PowerSupplier calculatePowerSupplier(final Long portId, final Double powerUsage, final PowerSupplier currentPowerSupplier) {
        Port port = portService.getPortById(portId);
        BatterySwitchOption batterySwitchOption = port.getBatterySwitchOption();

        BatterySwitchOptionType batterySwitchOptionType = batterySwitchOption.getBatterySwitchOptionType();
        if (batterySwitchOptionType.equals(BatterySwitchOptionType.OPTION_TIME)){
            // OPTION_TIME에 대한 복잡한 로직 처리
            LoadLevel currentLoadLevel = determineCurrentLoadLevel();
            String optionConfiguration = batterySwitchOption.getOptionConfiguration();

            Map<LoadLevel, PowerSupplier> map = parsingConfigurationOption(optionConfiguration);
            PowerSupplier powerSupplier = map.get(currentLoadLevel);

            return powerSupplier;
        } else if (batterySwitchOptionType.equals(BatterySwitchOptionType.OPTION_PREDICTION)){
            // OPTION_PREDICTION에 대한 로직 처리
            String optionConfiguration = batterySwitchOption.getOptionConfiguration();
            Integer ratio = Integer.parseInt(optionConfiguration);

            LocalDateTime convertedNow = localDateTimeService.getFormattedLocalDateTime(LocalDateTime.now());

            Double predictionPowerUsage = powerService.getPredictionPowerUsage(portId, convertedNow);

            if(powerUsage / predictionPowerUsage * 100 >= ratio)
                return PowerSupplier.BATTERY;
            else
                return PowerSupplier.EXTERNAL;
        } else if(batterySwitchOptionType.equals(BatterySwitchOptionType.OPTION_MANUAL)){
            return currentPowerSupplier;
        } else{
            log.error("유효하지 않은 배터리 스위치 옵션입니다. Current Option: {}",batterySwitchOptionType.toString());
            return currentPowerSupplier;
        }
    }

    public LoadLevel determineCurrentLoadLevel(){
        LocalTime now = LocalTime.now();
        MonthDay today = MonthDay.now();

        // 연중 시기 구분
        boolean isWinter = today.isAfter(MonthDay.of(10, 31)) || today.isBefore(MonthDay.of(3, 1));
        boolean isSummer = today.isAfter(MonthDay.of(5, 31)) && today.isBefore(MonthDay.of(9, 1));
        boolean isSpringFall = !isWinter && !isSummer;

        // 각 시기별 부하 시간대
        if (isWinter) {
            if (isBetween(now, "22:00", "08:00")) return LoadLevel.LOW;
            else if (isBetween(now, "08:00", "09:00") || isBetween(now, "12:00", "16:00") || isBetween(now, "19:00", "22:00")) return LoadLevel.MEDIUM;
            else return LoadLevel.HIGH;
        } else if (isSummer) {
            if (isBetween(now, "22:00", "08:00")) return LoadLevel.LOW;
            else if (isBetween(now, "08:00", "11:00") || isBetween(now, "12:00", "13:00") || isBetween(now, "18:00", "22:00")) return LoadLevel.MEDIUM;
            else return LoadLevel.HIGH;
        } else if (isSpringFall) {
            if (isBetween(now, "22:00", "08:00")) return LoadLevel.LOW;
            else if (isBetween(now, "11:00", "12:00") || isBetween(now, "13:00", "18:00")) return LoadLevel.MEDIUM;
            else return LoadLevel.HIGH;
        }

        throw new IllegalStateException("Unable to determine load level.");
    }

    private  boolean isBetween(LocalTime now, String start, String end) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime startTime = LocalTime.parse(start, formatter);
        LocalTime endTime = LocalTime.parse(end, formatter);
        return !now.isBefore(startTime) && !now.isAfter(endTime);
    }

    private Map parsingConfigurationOption(final String optionConfiguration){
        Map<LoadLevel, PowerSupplier> map = new HashMap<>();

        String[] parts = optionConfiguration.split(",");

        for (String part : parts) {
            String[] keyValue = part.split("-");
            if (keyValue.length == 2) {

                map.put(LoadLevel.valueOf(keyValue[0]), PowerSupplier.valueOf(keyValue[1]));
            }
        }

        return map;
    }
}
