package com.example.backendmainserver.event.domain;

import com.example.backendmainserver.port.domain.Port;
import com.example.backendmainserver.port.domain.PowerSupplier;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PowerSupplierChangedEvent extends ApplicationEvent {
    private final Port port;
    private final PowerSupplier beforePowerSupplier;
    private final PowerSupplier afterPowerSupplier;
    private final String message;

    public PowerSupplierChangedEvent(Object source,
                                     Port port,
                                     PowerSupplier beforePowerSupplier,
                                     PowerSupplier afterPowerSupplier,
                                     String message) {
        super(source);
        this.port = port;
        this.beforePowerSupplier = beforePowerSupplier;
        this.afterPowerSupplier = afterPowerSupplier;
        this.message = message;
    }
}