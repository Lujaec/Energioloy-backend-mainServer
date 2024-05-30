package com.example.backendmainserver.history.application.dto;


import com.example.backendmainserver.port.domain.PowerSupplier;

import java.time.LocalDateTime;

public record CreatePowerSupplierHistoryDto(
        LocalDateTime time,
        Long portId,
        PowerSupplier beforePowerSupplier,
        PowerSupplier afterPowerSupplier,
        String description
) {
}
