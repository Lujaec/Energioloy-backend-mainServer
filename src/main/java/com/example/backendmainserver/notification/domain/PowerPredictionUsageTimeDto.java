package com.example.backendmainserver.notification.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PowerPredictionUsageTimeDto {
    LocalDateTime time;
    Double powerPredictionUsage;
}
