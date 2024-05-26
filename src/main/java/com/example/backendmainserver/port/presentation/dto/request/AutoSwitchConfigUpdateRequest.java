package com.example.backendmainserver.port.presentation.dto.request;

import com.example.backendmainserver.port.domain.BatterySwitchOption;

public record AutoSwitchConfigUpdateRequest(Long portId, BatterySwitchOption batterySwitchOption) {
}
