package com.example.backendmainserver.PowerData.domain;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Getter
public class InMemoryPowerDataRepository {
    Map<Long, PowerDataList> store = new ConcurrentHashMap<>();

    @PostConstruct
    void initStore(){
        for(long i = 0; i < 10; ++i){
            store.put(i, PowerDataList.builder()
                    .powerDataList(new ArrayList<>())
                    .build());
        }
    }

    public void save(Long portId, PowerData powerData){
        PowerDataList powerDataList = store.get(portId);
        powerDataList.getPowerDataList().add(powerData);
    }

    public void delete(Long portId, int idx){
        PowerDataList powerDataList = store.get(portId);
        powerDataList.getPowerDataList().remove(idx);
    }
}
