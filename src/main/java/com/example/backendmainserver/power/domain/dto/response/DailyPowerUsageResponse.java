package com.example.backendmainserver.power.domain.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record DailyPowerUsageResponse (
     List<Double> powerUsageDataList,
     Double powerUsage,
     Double powerSupplierRatio
) {

}