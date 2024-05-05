package com.example.backendmainserver.power.domain.dto.response;

import lombok.Builder;

@Builder
public record MonthlyPowerUsageResponse(Double powerUsage, Double powerCost
                                        ) {

}
