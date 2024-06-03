package com.example.backendmainserver.port.presentation;

import com.example.backendmainserver.auth.presentation.AdminAuthenticationPrincipal;
import com.example.backendmainserver.client.raspberry.dto.request.BatterySwitchRequest;
import com.example.backendmainserver.client.raspberry.dto.request.PortAndSupplier;
import com.example.backendmainserver.global.response.SuccessResponse;
import com.example.backendmainserver.port.application.PortBatterySwitchService;
import com.example.backendmainserver.port.application.PortService;
import com.example.backendmainserver.port.domain.BatterySwitchOption;
import com.example.backendmainserver.port.domain.Port;
import com.example.backendmainserver.port.domain.PowerSupplier;
import com.example.backendmainserver.port.presentation.dto.request.AutoSwitchConfigUpdateRequest;
import com.example.backendmainserver.port.presentation.dto.request.PortControlRequest;
import com.example.backendmainserver.port.presentation.dto.request.PortIdAndState;
import com.example.backendmainserver.port.presentation.dto.response.PortInfoResponse;
import com.example.backendmainserver.port.presentation.dto.response.PortInfoResponses;
import com.example.backendmainserver.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Port API", description = "포트 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/port")
public class PortController {
    private final PortService portService;
    private final PortBatterySwitchService portBatterySwitchService;

    @Operation(summary = "포트 수동 제어 api", description =
            "라즈베리파이에 연결된 포트의 전원을 제어합니다.\n" +
            "state: BATTERY or EXTERNAL or OFF")
    @PostMapping("/control")
    public ResponseEntity<SuccessResponse<HttpStatus>> portControl(
            @AdminAuthenticationPrincipal User user,
            @RequestBody PortControlRequest portControlRequest){

        portBatterySwitchService.manualPortBatterySwitch(portControlRequest.portId(),
                portControlRequest.state());

        return SuccessResponse.of(HttpStatus.OK);
    }

    @PostMapping("/init/batterySupplier")
    public ResponseEntity<SuccessResponse<HttpStatus>> initBatterySupplier(){
        portBatterySwitchService.initAllPortBatterySupplier();
        return SuccessResponse.of(HttpStatus.OK);
    }

    @PatchMapping("/auto-switch")
    @Operation(summary = "포트 자동 제어 설정 변경 api", description =
            "포트 단위로 자동 제어 설정을 변경합니다.\n" +
                    "batterySwitchOptionType: OPTION_PREDICTION or OPTION_TIME\n" +
                    "optionConfiguration: 숫자 or LOW-전력공급원,MEDIUM-전력공급원,HIGH-전력공급원  ex: LOW-EXTERNAL,MEDIUM-EXTERNAL,HIGH-BATTERY")
    public ResponseEntity<SuccessResponse<HttpStatus>> updateAutoSwitchConfig(
            @AdminAuthenticationPrincipal User user,
            @RequestBody AutoSwitchConfigUpdateRequest request){

        portBatterySwitchService.updateAutoSwitchConfig(request.portId(), request.batterySwitchOption());
        return SuccessResponse.of(HttpStatus.OK);
    }

    @Operation(summary = "모든 포튼 정보 조회 List")
    @GetMapping("")
    public ResponseEntity<SuccessResponse<PortInfoResponses>> getAllPort(
            @AdminAuthenticationPrincipal User user
            ){
        List<Port> ports = portService.getAllPorts();
        List<PortInfoResponse> portInfoResponseList = convertToPortInfoResponseList(ports);
        return SuccessResponse.of(new PortInfoResponses(portInfoResponseList));
    }

    private List<PortInfoResponse> convertToPortInfoResponseList(List<Port> ports) {
        List<PortInfoResponse> list = ports.stream().map((p) -> {
            return new PortInfoResponse(
                    p.getId(),
                    p.getMinimumOutput(),
                    p.getMaximumOutput(),
                    p.getRoom().getId(),
                    p.getBatterySwitchOption(),
                    p.getPowerSupplier().toString());
        }).toList();

        return list;
    }


}
