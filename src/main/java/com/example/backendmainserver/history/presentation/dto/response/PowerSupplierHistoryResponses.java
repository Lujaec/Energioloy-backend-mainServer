package com.example.backendmainserver.history.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record PowerSupplierHistoryResponses(
        List<PowerSupplierHistoryResponse> powerSupplierHistoryResponseList
) {
}
