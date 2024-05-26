package com.example.backendmainserver.client.raspberry.dto.request;


import com.example.backendmainserver.port.domain.PowerSupplier;

public record PortAndSupplier(Long portId, PowerSupplier powerSupplier) {
}
