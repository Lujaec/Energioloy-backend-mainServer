package com.example.backendmainserver.PowerData.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
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

