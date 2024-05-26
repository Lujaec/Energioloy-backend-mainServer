package com.example.backendmainserver.power.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Data
public class Power {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "power_id")
    private Long id;

    @Column
    private Double powerPredictionUsage;

    @Column
    private Double powerUsage;

    @Column
    private Double powerCost;

    @Column
    private Double powerPredictionCost;

    @Column
    private String powerSupplier;

    @Column
    private Long portId;

    @Column
    private LocalDateTime time;

    @Builder
    public Power(Double powerUsage, Double powerCost, String powerSupplier, Long portId, LocalDateTime time) {
        this.powerUsage = powerUsage;
        this.powerCost = powerCost;
        this.powerSupplier = powerSupplier;
        this.portId = portId;
        this.time = time;
    }
}
