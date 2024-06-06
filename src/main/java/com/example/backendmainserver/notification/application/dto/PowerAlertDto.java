package com.example.backendmainserver.notification.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public class PowerAlertDto {
    private Long roomId;
    private Long portId;
    private String fcmToken;
    private double powerUsage;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        PowerAlertDto that = (PowerAlertDto) o;
        return Objects.equals(roomId, that.roomId) &&
                Objects.equals(portId, that.portId) &&
                Objects.equals(fcmToken, that.fcmToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomId, portId, fcmToken);
    }
}
