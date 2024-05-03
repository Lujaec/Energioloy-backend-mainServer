package com.example.backendmainserver.PowerData.domain;

import com.example.backendmainserver.PowerData.domain.PowerData;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PowerDataList {
    private List<PowerData> powerDataList;

    public PowerDataList() {
    }

    @Override
    public String toString() {
        return "PowerDataList{" +
                "powerDataList=" + powerDataList.toString() +
                '}';
    }

    @Builder
    public PowerDataList(List<PowerData> powerDataList) {
        this.powerDataList = powerDataList;
    }
}