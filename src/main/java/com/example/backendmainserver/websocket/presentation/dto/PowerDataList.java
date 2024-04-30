package com.example.backendmainserver.websocket.presentation.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PowerDataList {
    private List<PowerData> powerDataList;

    @Override
    public String toString() {
        return "PowerDataList{" +
                "powerDataList=" + powerDataList.toString() +
                '}';
    }
}