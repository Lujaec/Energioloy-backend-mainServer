package com.example.backendmainserver.port.presentation.dto.request;

import com.example.backendmainserver.port.domain.PowerSupplier;


public record PortControlRequest(Long portId, PowerSupplier state) {
}
