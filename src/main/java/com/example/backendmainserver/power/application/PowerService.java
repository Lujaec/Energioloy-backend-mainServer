package com.example.backendmainserver.power.application;

import com.example.backendmainserver.PowerData.application.PowerDataService;
import com.example.backendmainserver.PowerData.domain.PowerData;
import com.example.backendmainserver.PowerData.domain.PowerDataList;
import com.example.backendmainserver.power.domain.Power;
import com.example.backendmainserver.power.domain.PowerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PowerService {
    private final PowerDataService powerDataService;
    private final PowerRepository powerRepository;
    private final int MAX_PORT_CNT = 5;
    @Scheduled(cron = "0 * * * * *")
    public void convertPowerPerMinute() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime convertedNow = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour(), now.getMinute());
        log.info("convertedNow={}", convertedNow.toString());

        Map<Long, PowerDataList> store = powerDataService.getStore();

        for(long i = 1; i < MAX_PORT_CNT + 1; ++i){
            PowerDataList powerDataList = store.get(i);

            List<PowerData> powerDataL = powerDataList.getPowerDataList();

            double totalPower = calTotalPower(powerDataL, convertedNow);
            Long portId = i;
            String powerSupplier = calPowerSupplier(powerDataL);

            Power powerPerMinute = Power.builder()
                    .powerUsage(totalPower)
                    .powerSupplier(powerSupplier)
                    .portId(portId)
                    .time(convertedNow)
                    .build();

            this.save(powerPerMinute);
        }

        powerDataService.deleteDataBeforeTime(convertedNow);
    }

    private double calTotalPower(List<PowerData> powerDataL, LocalDateTime conventedNow){
        double total = 0;
        int cnt = 0;

        for (PowerData powerData : powerDataL) {
            total += powerData.getPower();

            if (powerData.getTime().isBefore(conventedNow))
                cnt += 1;
        }

        if(cnt == 0)
            return 0;

           total = total / cnt * 60;
        return total;
    }

    private String calPowerSupplier(List<PowerData> powerDataL){
        Map<String, Integer> supplierCnt = new HashMap<>();

        for (PowerData powerData : powerDataL) {
            String supplier = powerData.getPowerSupplier();
            supplierCnt.put(supplier, supplierCnt.getOrDefault(supplier, 0) + 1);
        }

        String mostCommonSupplier = null;
        int maxCount = 0;

        for (Map.Entry<String, Integer> entry : supplierCnt.entrySet()) {
            if (entry.getValue() > maxCount) {
                mostCommonSupplier = entry.getKey();
                maxCount = entry.getValue();
            }
        }

        return mostCommonSupplier;
    }

    @Transactional
    public Power save(Power powerPerMinute){
        return powerRepository.save(powerPerMinute);
    }
}
