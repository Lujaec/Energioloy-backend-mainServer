package com.example.backendmainserver.history.presentation;


import com.example.backendmainserver.auth.presentation.AuthenticationPrincipal;
import com.example.backendmainserver.global.response.SuccessResponse;
import com.example.backendmainserver.history.application.PowerSupplierHistoryService;
import com.example.backendmainserver.history.domain.PowerSupplierHistory;
import com.example.backendmainserver.history.presentation.dto.response.PowerSupplierHistoryResponse;
import com.example.backendmainserver.history.presentation.dto.response.PowerSupplierHistoryResponses;
import com.example.backendmainserver.port.domain.PowerSupplier;
import com.example.backendmainserver.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/powerSupplierHistory")
@Slf4j
public class PowerSupplierHistoryController {
    private final PowerSupplierHistoryService powerSupplierHistoryService;

    @GetMapping("")
    public ResponseEntity<SuccessResponse<PowerSupplierHistoryResponses>> getHistoryByRoomId(@AuthenticationPrincipal User user){
        Long roomId = user.getRoom().getId();
        List<PowerSupplierHistory> powerSupplierHistoryList = powerSupplierHistoryService.getHistoriesByRoomId(roomId);

        PowerSupplierHistoryResponses responses = convertToResponses(powerSupplierHistoryList);
        return SuccessResponse.of(responses);
    }

    private String getStringPowerSupplier(PowerSupplier powerSupplier){
        if (powerSupplier.equals(PowerSupplier.BATTERY))
            return "배터리";
        else if(powerSupplier.equals(PowerSupplier.EXTERNAL))
            return "외부전력";
        else if(powerSupplier.equals(PowerSupplier.OFF))
            return "차단";

        return "ERROR";
    }
    private PowerSupplierHistoryResponse convertToResponse(PowerSupplierHistory powerSupplierHistory){
        String beforePowerSupplier = getStringPowerSupplier(powerSupplierHistory.getBeforePowerSupplier());
        String afterPowerSupplier = getStringPowerSupplier(powerSupplierHistory.getAfterPowerSupplier());

        return new PowerSupplierHistoryResponse(
                powerSupplierHistory.getTime(),
                powerSupplierHistory.getPort().getId(),
                beforePowerSupplier,
                afterPowerSupplier,
                powerSupplierHistory.getDescription()
        );
    }
    private PowerSupplierHistoryResponses convertToResponses(List<PowerSupplierHistory> powerSupplierHistoryList){
        List<PowerSupplierHistoryResponse> supplierHistoryResponseList = powerSupplierHistoryList.stream().map(
                e -> convertToResponse(e)
        ).toList();

        return new PowerSupplierHistoryResponses(supplierHistoryResponseList);
    }
}
