package com.example.backendmainserver.PowerData.domain;

import com.example.backendmainserver.PowerData.domain.PowerData;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PowerDataList {
    private List<PowerData> powerDataList;

    @Override
    public String toString() {
        return "PowerDataList{" +
                "powerDataList=" + powerDataList.toString() +
                '}';
    }
}