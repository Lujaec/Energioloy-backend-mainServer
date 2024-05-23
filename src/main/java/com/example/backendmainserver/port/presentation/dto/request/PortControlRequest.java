package com.example.backendmainserver.port.presentation.dto.request;

import java.util.List;

public record PortControlRequest(List<PortIdAndState> portIdAndStates) {
}
