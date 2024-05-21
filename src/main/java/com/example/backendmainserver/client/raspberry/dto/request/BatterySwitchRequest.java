package com.example.backendmainserver.client.raspberry.dto.request;


import java.util.List;

public record BatterySwitchRequest(List<PortAndSupplier> portAndSuppliers) {

}


