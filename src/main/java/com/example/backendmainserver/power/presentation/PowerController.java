package com.example.backendmainserver.power.presentation;

import com.example.backendmainserver.auth.presentation.AuthenticationPrincipal;
import com.example.backendmainserver.global.response.SuccessResponse;
import com.example.backendmainserver.power.application.PowerService;
import com.example.backendmainserver.power.domain.dto.response.DailyPowerPredictionResponse;
import com.example.backendmainserver.power.domain.dto.response.DailyPowerUsageResponse;
import com.example.backendmainserver.power.domain.dto.response.MonthlyPowerPredictionResponse;
import com.example.backendmainserver.power.domain.dto.response.MonthlyPowerUsageResponse;
import com.example.backendmainserver.room.application.RoomService;
import com.example.backendmainserver.room.domain.Room;
import com.example.backendmainserver.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/power")
@Tag(name = "전력 API", description = "전력 관련 api입니다.")
@Slf4j
public class PowerController {
    private final PowerService powerService;
    private final RoomService roomService;

    @Operation(summary="이번달 전력 사용량, 요금 조회 api", description = "이번 달 사용한 전력량 및 요금을 조회하는 api입니다.")
    @GetMapping("/usage/month/")
    public ResponseEntity<SuccessResponse<MonthlyPowerUsageResponse>> getMonthlyPowerUsage(@AuthenticationPrincipal User user) {
        return SuccessResponse.of(powerService.getMonthlyPowerUsageWithAllRoom());
    }

    @Operation(summary="이번달 예정 사용량, 요금 조회 api", description = "이번 달 사용할 예정인 전력량 및 요금을 조회하는 api입니다.")
    @GetMapping("/prediction/month")
    public ResponseEntity<SuccessResponse<MonthlyPowerPredictionResponse>> getMonthlyPowerPrediction(@AuthenticationPrincipal User user) {
        return SuccessResponse.of(powerService.getMonthlyPowerPredictionWithAllRoom());
    }

    @Operation(summary="오늘 전력 사용량, 공급원 비율 조회 api", description = "오늘 사용 전략량, 공급원비율조회하는 api입니다.")
    @GetMapping("/usage/today")
    public ResponseEntity<SuccessResponse<DailyPowerUsageResponse>> getDailyPowerUsage(@AuthenticationPrincipal User user) {
        return SuccessResponse.of(powerService.getDailyPowerUsageWithAllRoom());
    }
    @Operation(summary="오늘 전력 예측량 조회 api", description = "오늘 전력 예측량 조회 api입니다.")
    @GetMapping("/prediction/today")
    public ResponseEntity<SuccessResponse<DailyPowerPredictionResponse>> getDailyPowerPrediction(@AuthenticationPrincipal User user) {
        return SuccessResponse.of(powerService.getDailyPowerPredictionWithAllRoom());
    }

    //////////with room

    /**
     * @param user
     * @param roomId
     * 0 -> 자신이 소유한 방을 조회
     * n -> 타인의 방인경우 예외 발생, 단, 관리자인 경우 조회가능
     * @return
     */

    @Operation(summary="이번달 전력 사용량 방 별, 요금 조회 api", description = "이번 달 사용한 전력량 및 요금을 방 별 조회하는 api입니다.")
    @GetMapping("/room/{roomId}/usage/month/")
    public ResponseEntity<SuccessResponse<MonthlyPowerUsageResponse>> getMonthlyPowerUsageWithRoom(@AuthenticationPrincipal User user,
                                                                                           @PathVariable Long roomId) {
        Room room = roomService.getRoomOfUser(user, roomId);
        return SuccessResponse.of(powerService.getMonthlyPowerUsageWithRoom(room));
    }

    @Operation(summary="이번달 예정 사용량 방 별, 요금 조회 api", description = "이번 달 사용할 예정인 전력량 및 요금을 방 별 조회하는 api입니다.")
    @GetMapping("/room/{roomId}/prediction/month")
    public ResponseEntity<SuccessResponse<MonthlyPowerPredictionResponse>> getMonthlyPowerPredictionWithRoom(@AuthenticationPrincipal User user,
                                                                                                             @PathVariable Long roomId) {
        Room room = roomService.getRoomOfUser(user, roomId);
        return SuccessResponse.of(powerService.getMonthlyPowerPredictionWithRoom(room));
    }

    @Operation(summary="오늘 전력 사용량 방 별, 공급원 비율 조회 api", description = "오늘 사용 전략량 방 별, 공급원비율조회하는 api입니다.")
    @GetMapping("/room/{roomId}usage/today")
    public ResponseEntity<SuccessResponse<DailyPowerUsageResponse>> getDailyPowerUsageWithRoom(@AuthenticationPrincipal User user,
                                                                                               @PathVariable Long roomId) {
        Room room = roomService.getRoomOfUser(user, roomId);
        return SuccessResponse.of(powerService.getDailyPowerUsageWithRoom(room));
    }
    @Operation(summary="오늘 전력 예측량 방 별 조회 api", description = "오늘 전력 예측량 방 별 조회 api입니다.")
    @GetMapping("/room/{roomId}/prediction/today")
    public ResponseEntity<SuccessResponse<DailyPowerPredictionResponse>> getDailyPowerPredictionWithRoom(@AuthenticationPrincipal User user,
                                                                                                         @PathVariable Long roomId) {
        Room room = roomService.getRoomOfUser(user, roomId);
        return SuccessResponse.of(powerService.getDailyPowerPredictionWithRoom(room));
    }
}
