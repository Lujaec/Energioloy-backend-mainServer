package com.example.backendmainserver.port.presentation.dto.response;

import com.example.backendmainserver.port.domain.BatterySwitchOption;

public record PortInfoResponse(Long portId,
                               Long minimumOutput,
                               Long maximumOutput,
                               Long roomId,
                               BatterySwitchOption batterySwitchOption,
                               String powerSupplier) {
}
