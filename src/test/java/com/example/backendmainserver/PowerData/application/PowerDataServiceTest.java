package com.example.backendmainserver.PowerData.application;

import com.example.backendmainserver.PowerData.domain.InMemoryPowerDataRepository;
import com.example.backendmainserver.PowerData.domain.PowerData;
import com.example.backendmainserver.PowerData.domain.PowerDataList;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@SpringBootTest
class PowerDataServiceTest {
    @Autowired
    private  PowerDataService powerDataService;
    @Autowired
    private InMemoryPowerDataRepository inMemoryPowerDataRepository;

    @Test
    @DisplayName("데이터가 저장되는지 확인")
    void storeData(){
        //given
        PowerData pd1 = PowerData.builder()
                .time(LocalDateTime.now())
                .portId(1L)
                .power(1.0)
                .powerSupplier("external").build();

        PowerData pd2 = PowerData.builder()
                .time(LocalDateTime.now())
                .portId(2L)
                .power(1.0)
                .powerSupplier("external").build();

        PowerData pd3 = PowerData.builder()
                .time(LocalDateTime.now())
                .portId(3L)
                .power(1.0)
                .powerSupplier("external").build();

        PowerData pd4 = PowerData.builder()
                .time(LocalDateTime.now())
                .portId(4L)
                .power(1.0)
                .powerSupplier("external").build();

        PowerData pd5 = PowerData.builder()
                .time(LocalDateTime.now())
                .portId(5L)
                .power(1.0)
                .powerSupplier("external").build();

        PowerDataList dataList = PowerDataList.builder()
                .powerDataList(List.of(pd1, pd2, pd3, pd4, pd5))
                .build();

        //when
        powerDataService.storeData(dataList);

        //then
        Map<Long, PowerDataList> store = inMemoryPowerDataRepository.getStore();

        for(long i = 1; i < 6; i ++){
            PowerDataList powerDataList = store.get(i);

            Assertions.assertThat(powerDataList.getPowerDataList().size()).isEqualTo(1);
            checkContent(dataList.getPowerDataList().get((int) i - 1));
        }
    }

    private void checkContent(PowerData powerData){
        Map<Long, PowerDataList> store = inMemoryPowerDataRepository.getStore();

        PowerDataList powerDataList = store.get(powerData.getPortId());
        List<PowerData> pdl = powerDataList.getPowerDataList();
        Assertions.assertThat(pdl.get(pdl.size() - 1)).isEqualTo(powerData);
    }
}