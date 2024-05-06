package com.example.backendmainserver.power.presentation;

import com.example.backendmainserver.global.response.SuccessResponse;
import com.example.backendmainserver.power.application.PowerService;
import com.example.backendmainserver.power.domain.dto.response.DailyPowerPredictionResponse;
import com.example.backendmainserver.power.domain.dto.response.DailyPowerUsageResponse;
import com.example.backendmainserver.power.domain.dto.response.MonthlyPowerPredictionResponse;
import com.example.backendmainserver.power.domain.dto.response.MonthlyPowerUsageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/power")
@Tag(name = "전력 API", description = "전력 관련 api입니다.")
public class PowerController {
    private final PowerService powerService;

    @Operation(summary="이번달 전력 사용량, 요금 조회 api", description = "이번 달 사용한 전력량 및 요금을 조회하는 api입니다.")
    @GetMapping("/usage/month")
    public ResponseEntity<SuccessResponse<MonthlyPowerUsageResponse>> getMonthlyPowerUsage() {
        return SuccessResponse.of(powerService.getMonthlyPowerUsage());
    }

    @Operation(summary="이번달 예정 사용량, 요금 조회 api", description = "이번 달 사용할 예정인 전력량 및 요금을 조회하는 api입니다.")
    @GetMapping("/prediction/month")
    public ResponseEntity<SuccessResponse<MonthlyPowerPredictionResponse>> getMonthlyPowerPrediction() {
        return SuccessResponse.of(powerService.getMonthlyPowerPrediction());
    }

    @Operation(summary="오늘 전력 사용량, 공급원 비율 조회 api", description = "오늘 사용 전략량, 공급원비율조회하는 api입니다.")
    @GetMapping("/usage/today")
    public ResponseEntity<SuccessResponse<DailyPowerUsageResponse>> getDailyPowerUsage() {
        return SuccessResponse.of(powerService.getDailyPowerUsage());
    }
    @Operation(summary="오늘 전력 예측량 조회 api", description = "오늘 전력 예측량 조회 api입니다.")
    @GetMapping("/prediction/today")
    public ResponseEntity<SuccessResponse<DailyPowerPredictionResponse>> getDailyPowerPrediction() {
        return SuccessResponse.of(powerService.getDailyPowerPrediction());
    }



}
