package com.example.backendmainserver.power.domain.dto.response;

import lombok.Builder;

@Builder
public record DailyPowerUsageResponse(Double powerUsage, Double powerPredictionUsage ,
                                      Double powerSupplierRatio) {
}
