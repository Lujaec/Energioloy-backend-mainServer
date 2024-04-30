package com.example.backendmainserver.websocket.presentation.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PowerData {
    LocalDateTime time;
    Long portId;
    Double power;
    String powerSupplier;

    @Override
    public String toString() {
        return "PowerData{" +
                "time=" + time +
                ", portId=" + portId +
                ", power=" + power +
                ", powerSupplier='" + powerSupplier + '\'' +
                '}';
    }
}

