package com.example.backendmainserver.power.application;

import com.example.backendmainserver.PowerData.application.PowerDataService;
import com.example.backendmainserver.PowerData.domain.PowerData;
import com.example.backendmainserver.PowerData.domain.PowerDataList;
import com.example.backendmainserver.power.domain.Power;
import com.example.backendmainserver.power.domain.PowerRepository;
import com.example.backendmainserver.power.domain.dto.response.DailyPowerPredictionResponse;
import com.example.backendmainserver.power.domain.dto.response.DailyPowerUsageResponse;
import com.example.backendmainserver.power.domain.dto.response.MonthlyPowerPredictionResponse;
import com.example.backendmainserver.power.domain.dto.response.MonthlyPowerUsageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
     * 이번 달 전력 사용량, 요금 조회 로직
     * @return
     */
    public MonthlyPowerUsageResponse getMonthlyPowerUsage() {
        List<Power> currentMonthPower = powerRepository.findCurrentMonthPower();

        Double sumPowerUsage = 0.0;
        Double sumPowerCost = 0.0;


        for (Power power : currentMonthPower){
            if (power.getPowerUsage() != null) {
                sumPowerUsage += power.getPowerUsage();
            }
            if (power.getPowerCost() != null) {
                sumPowerCost += power.getPowerCost();
            }

        }

        return MonthlyPowerUsageResponse.builder()
                .powerCost(sumPowerCost)
                .powerUsage(sumPowerUsage)
                .build();
    }

    /**
     * 이번 달 전력 예측량, 요금 데이터 조회 로직
     * @return
     */
    public MonthlyPowerPredictionResponse getMonthlyPowerPrediction() {
        List<Power> currentMonthPower = powerRepository.findCurrentMonthPower();

        Double sumPowerPredictionUsage= 0.0;
        Double sumPowerPredictionCost = 0.0;

        for (Power power : currentMonthPower){
            if (power.getPowerPredictionUsage() != null) {
                sumPowerPredictionUsage += power.getPowerPredictionUsage();
            }
            if (power.getPowerPredictionCost() != null) {
                sumPowerPredictionCost += power.getPowerPredictionCost();
            }
        }

        return MonthlyPowerPredictionResponse.builder()
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
        List<Double> powerUsageList = new ArrayList<>() ;

        double sumPowerUsage = 0.0;
        double externalRatio = 100.0;
        int batteryCount = 0;
        int externalCount = 0;

        for (Power power : currentDailyPower) {
            powerUsageList.add(power.getPowerUsage());
            if (power.getPowerUsage() != null) {
                sumPowerUsage += power.getPowerUsage();
            }
            if (power.getPowerSupplier() != null) {
                if (power.getPowerSupplier().equals("battery")) {
                    batteryCount += 1;
                } else if (power.getPowerSupplier().equals("external")) {
                    externalCount += 1;
                }

            }
        }
        if( batteryCount + externalCount !=0){
            externalRatio = (double) externalCount / (batteryCount + externalCount) * 100;
        }

        return DailyPowerUsageResponse.builder()
                .powerUsage(sumPowerUsage)
                .powerUsageDataList(powerUsageList)
                .powerSupplierRatio(externalRatio)
                .build();
    }

    /**
     * 오늘 예측량 데이터 조회 로직
     * @return
     */
    public DailyPowerPredictionResponse getDailyPowerPrediction() {
        List<Power> currentDailyPower = powerRepository.findTodayPower();
        List<Double> powerPredictionList = new ArrayList<>();


        double sumPowerPredictionUsage= 0.0;

        for (Power power : currentDailyPower) {
            powerPredictionList.add(power.getPowerPredictionUsage());
            if (power.getPowerPredictionUsage() != null) {
                sumPowerPredictionUsage += power.getPowerPredictionUsage();
            }
        }

        return DailyPowerPredictionResponse.builder()
                .powerPredictionUsage(sumPowerPredictionUsage)
                .powerPredictionDataList(powerPredictionList)
                .build();
    }
}
