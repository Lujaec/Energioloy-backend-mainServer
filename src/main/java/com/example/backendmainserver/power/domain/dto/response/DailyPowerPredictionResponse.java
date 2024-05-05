package com.example.backendmainserver.power.domain.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class DailyPowerPredictionResponse {
    private List<Double> powerPredictionDataList;
    private Double powerPredictionUsage;


}