package com.example.backendmainserver.client.raspberry.dto.response;

import java.util.List;

public record BatterySwitchResponse(
        List<PortAndResult> portAndResults
) {
}
