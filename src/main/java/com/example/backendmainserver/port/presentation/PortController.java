package com.example.backendmainserver.port.presentation;

import com.example.backendmainserver.auth.presentation.AdminAuthenticationPrincipal;
import com.example.backendmainserver.client.raspberry.dto.request.BatterySwitchRequest;
import com.example.backendmainserver.client.raspberry.dto.request.PortAndSupplier;
import com.example.backendmainserver.global.response.SuccessResponse;
import com.example.backendmainserver.port.application.PortBatterySwitchService;
import com.example.backendmainserver.port.application.PortService;
import com.example.backendmainserver.port.presentation.dto.request.PortControlRequest;
import com.example.backendmainserver.user.domain.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Port API", description = "포트 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/port")
public class PortController {
    private final PortService portService;
    private final PortBatterySwitchService portBatterySwitchService;

    @PostMapping("/control")
    public ResponseEntity<SuccessResponse<HttpStatus>> portControl(
            @AdminAuthenticationPrincipal User user,
            @RequestBody PortControlRequest portControlRequest){

        BatterySwitchRequest batterySwitchRequest = convertToBatterySwitchRequest(portControlRequest);
        portBatterySwitchService.requestBatterySwitchToRaspberry(batterySwitchRequest);

        return SuccessResponse.of(HttpStatus.OK);
    }

    public BatterySwitchRequest convertToBatterySwitchRequest(PortControlRequest portControlRequest) {
        List<PortAndSupplier> portAndSuppliers = portControlRequest.portIdAndStates().stream()
                .map(portIdAndState -> new PortAndSupplier(portIdAndState.portId(), portIdAndState.state()))
                .collect(Collectors.toList());

        return new BatterySwitchRequest(portAndSuppliers);
    }
}
