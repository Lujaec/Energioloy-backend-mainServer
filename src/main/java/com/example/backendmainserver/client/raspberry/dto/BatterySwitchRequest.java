package com.example.backendmainserver.client.raspberry.dto;


import java.util.List;

public record BatterySwitchRequest(List<PortAndSupplier> portAndSuppliers) {

}


