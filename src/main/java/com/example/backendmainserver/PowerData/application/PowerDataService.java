package com.example.backendmainserver.PowerData.application;

import com.example.backendmainserver.PowerData.domain.InMemoryPowerDataRepository;
import com.example.backendmainserver.PowerData.domain.PowerData;
import com.example.backendmainserver.PowerData.domain.PowerDataList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class PowerDataService {
    private final InMemoryPowerDataRepository powerDataRepository;
    public void storeData(PowerDataList dataList){
        List<PowerData> powerDataList = dataList.getPowerDataList();

        powerDataList.stream().forEach(
                (powerData)->{
                    Long portId = powerData.getPortId();
                    powerDataRepository.save(portId,powerData);
                }
        );
    }

    public void deleteDataBeforeTime(LocalDateTime time){
        Map<Long, PowerDataList> store = powerDataRepository.getStore();

        for (PowerDataList powerDataList : store.values()) {
            List<PowerData> powerDataL = powerDataList.getPowerDataList();
            int size = powerDataL.size();

            for(int i = size - 1; i >= 0; --i){
                PowerData powerData = powerDataL.get(i);

                Long portId = powerData.getPortId();
                LocalDateTime measurementTime = powerData.getTime();

                if(measurementTime.isBefore(time)){
                    powerDataRepository.delete(portId, i);
                }
            }
        }
    }

    public Map<Long, PowerDataList> getStore(){
        return powerDataRepository.getStore();
    }
}
