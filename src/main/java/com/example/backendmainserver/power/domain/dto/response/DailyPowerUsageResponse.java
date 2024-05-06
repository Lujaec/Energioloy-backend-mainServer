package com.example.backendmainserver.power.domain.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public class DailyPowerUsageResponse {
    private List<Double> powerUsageDataList;
    private Double powerUsage;
    private Double powerSupplierRatio;
}