package com.example.backendmainserver.power.application;

import com.example.backendmainserver.PowerData.application.PowerDataService;
import com.example.backendmainserver.PowerData.domain.PowerData;
import com.example.backendmainserver.PowerData.domain.PowerDataList;
import com.example.backendmainserver.power.domain.Power;
import com.example.backendmainserver.power.domain.PowerRepository;
import com.example.backendmainserver.power.domain.dto.response.DailyPowerUsageResponse;
import com.example.backendmainserver.power.domain.dto.response.MonthlyPowerUsageResponse;
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

            Power existingPower = powerRepository.findByPortIdAndTime(portId, convertedNow)
                    .orElseThrow(() -> new IllegalStateException("portId와 시간으로 Power row를 찾을 수없습니다"));

            existingPower.setPowerUsage(totalPower);
            existingPower.setPowerSupplier(powerSupplier);
            existingPower.setPowerCost(totalPower * 20); //임의의 가중치 20
            powerRepository.save(existingPower);



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

        String mostCommonSupplier = "external"; //기본값!
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

    /**
     * 이번 달 전력 사용량, 예측량 데이터 조회 로직
     * @return
     */
    public MonthlyPowerUsageResponse getMonthlyPowerUsage() {
        List<Power> currentMonthPower = powerRepository.findCurrentMonthPower();

        Double sumPowerUsage = 0.0;
        Double sumPowerCost = 0.0;
        Double sumPowerPredictionUsage= 0.0;
        Double sumPowerPredictionCost = 0.0;

        for (Power power : currentMonthPower){
            sumPowerUsage += power.getPowerUsage();
            sumPowerCost += power.getPowerCost();
            sumPowerPredictionUsage += power.getPowerPredictionUsage();
            sumPowerPredictionCost += power.getPowerPredictionCost();
        }

        return MonthlyPowerUsageResponse.builder()
                .powerCost(sumPowerCost)
                .powerUsage(sumPowerUsage)
                .powerPredictionUsage(sumPowerPredictionUsage)
                .powerPredictionCost(sumPowerPredictionCost)
                .build();
    }

    /**
     * 오늘 전력 사용량, 예측량 데이터 조회 로직
     * @return
     */
    public DailyPowerUsageResponse getDailyPowerUsage() {
        List<Power> currentDailyPower = powerRepository.findTodayPower();

        Double sumPowerUsage = 0.0;
        Double sumPowerPredictionUsage= 0.0;
        int batteryCount = 0;
        int externalCount = 0;

        for (Power power : currentDailyPower){
            sumPowerUsage += power.getPowerUsage();
            sumPowerPredictionUsage += power.getPowerPredictionUsage();
            if (power.getPowerSupplier().equals("battery")) {
                batteryCount += 1;
            } else if (power.getPowerSupplier().equals("external")) {
                externalCount += 1;
            }
            else{
                log.error("Battery or External이 아닌 공급원이 발견되었습니다.");
            }
        }

        return DailyPowerUsageResponse.builder()
                .powerUsage(sumPowerUsage)
                .powerPredictionUsage(sumPowerPredictionUsage)
                .powerSupplierRatio((double) externalCount / currentDailyPower.size() * 100)
                .build();
    }

}
