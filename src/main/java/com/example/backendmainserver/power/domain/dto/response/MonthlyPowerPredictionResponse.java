package com.example.backendmainserver.power.domain.dto.response;

import lombok.Builder;

@Builder
public record MonthlyPowerPredictionResponse(Double powerPredictionUsage ,
                                             Double powerPredictionCost) {
}
