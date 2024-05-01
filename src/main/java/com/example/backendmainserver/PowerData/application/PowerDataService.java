package com.example.backendmainserver.PowerData.application;

import com.example.backendmainserver.PowerData.domain.InMemoryPowerDataRepository;
import com.example.backendmainserver.PowerData.domain.PowerData;
import com.example.backendmainserver.PowerData.domain.PowerDataList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
