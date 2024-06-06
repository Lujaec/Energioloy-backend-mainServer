package com.example.backendmainserver.notification.application;

import com.example.backendmainserver.fcm.application.FcmService;
import com.example.backendmainserver.fcm.presentation.dto.MessageDto;
import com.example.backendmainserver.global.application.LocalDateTimeService;
import com.example.backendmainserver.notification.application.dto.PowerAlertDto;
import com.example.backendmainserver.notification.domain.PowerPredictionUsageTimeDto;
import com.example.backendmainserver.power.application.PowerService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class PowerAlertService {
    private final LocalDateTimeService localDateTimeService;
    private final PowerService powerService;
    private final FcmService fcmService;
    private ConcurrentHashMap<PowerAlertDto, Long> lastAlertTimeMap = new ConcurrentHashMap<>();
    private Map<Long, Long> roomNumberMap = new ConcurrentHashMap<>();
    private Map<Long, PowerPredictionUsageTimeDto> portIdAndPowerPredictionUsageTimeMap = new ConcurrentHashMap<>();

    private final String ALERT_TITLE ="⚠️ 비정상 전력 사용이 감지되었습니다 ⚠️";

    @PostConstruct()
    void init() {
        roomNumberMap.put(2L, 101L);
        roomNumberMap.put(3L, 102L);
        updatePowerUsage();
    }

    @Scheduled(cron = "0 * * * * *")
    public void updatePowerUsageEveryMinute() {
        updatePowerUsage();
    }

    private void updatePowerUsage() {
        LocalDateTime formattedLocalDateTime = localDateTimeService.getFormattedLocalDateTime(LocalDateTime.now());

        Map<Long, Double> portIdAndPowerPredictionUsageMap = new ConcurrentHashMap<>(powerService.getPowerPredictionUsageAllPorts(formattedLocalDateTime));

        for (Long portId : portIdAndPowerPredictionUsageMap.keySet()) {
            Double powerPredictionUsage = portIdAndPowerPredictionUsageMap.get(portId);

            portIdAndPowerPredictionUsageTimeMap.put(portId, new PowerPredictionUsageTimeDto(formattedLocalDateTime, powerPredictionUsage));
        }
    }

    public void alert(PowerAlertDto powerAlertDto) {
        LocalDateTime formattedLocalDateTime = localDateTimeService.getFormattedLocalDateTime(LocalDateTime.now());
        PowerPredictionUsageTimeDto powerPredictionUsageTimeDto = portIdAndPowerPredictionUsageTimeMap.get(powerAlertDto.getPortId());

        if(powerPredictionUsageTimeDto == null || !formattedLocalDateTime.isEqual(powerPredictionUsageTimeDto.getTime()))
            return;

        if(powerAlertDto.getPowerUsage() / 1000.0 < 1 * powerPredictionUsageTimeDto.getPowerPredictionUsage())
            return;

        long currentTime = System.currentTimeMillis();
        Long lastAlertTime = lastAlertTimeMap.getOrDefault(powerAlertDto, 0L);

        if (currentTime - lastAlertTime > TimeUnit.MINUTES.toMillis(5)) {
            fcmService.sendMessage(new MessageDto(powerAlertDto.getFcmToken(),
                    ALERT_TITLE,
                    createAlertMessage(powerAlertDto.getRoomId(), powerAlertDto.getPortId())));
            lastAlertTimeMap.put(powerAlertDto, currentTime);
        }
    }

    private String createAlertMessage(Long roomId, Long portId){
        Long roomNumber = roomNumberMap.get(roomId);
        return roomNumber + "번 방 " + portId + "번 포트에서 비정상 전력 사용 감지";
    }
}
