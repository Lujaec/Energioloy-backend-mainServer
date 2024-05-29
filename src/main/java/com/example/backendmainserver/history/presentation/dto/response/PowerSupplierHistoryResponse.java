package com.example.backendmainserver.history.presentation.dto.response;

import java.time.LocalDateTime;

public record PowerSupplierHistoryResponse(
    LocalDateTime time,
    Long portId,
    String beforePowerSupplier,
    String afterPowerSupplier,
    String description
) {
}
