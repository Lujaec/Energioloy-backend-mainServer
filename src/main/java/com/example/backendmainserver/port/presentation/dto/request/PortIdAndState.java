package com.example.backendmainserver.port.presentation.dto.request;

import com.example.backendmainserver.port.domain.PowerSupplier;

/**
 * id: portId를 의미
 * state:
 *  - BATTERY: 배터리로 공급
 *  - EXTERNAL: 외부 전력으로 공급
 *  - OFF: 전력 차단
 */
public record PortIdAndState(Long portId, PowerSupplier state) {
}
