package com.example.backendmainserver.event.domain;

import com.example.backendmainserver.port.domain.Port;
import com.example.backendmainserver.port.domain.PowerSupplier;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PowerAlertEvent extends ApplicationEvent {
    private Long roomId;
    private Long portId;
    private String fcmToken;
    private Double powerUsage;

    public PowerAlertEvent(Object source,
                           Long roomId,
                           Long portId,
                           String fcmToken,
                           Double powerUsage) {
        super(source);
        this.roomId = roomId;
        this.portId = portId;
        this.fcmToken = fcmToken;
        this.powerUsage = powerUsage;
    }
}