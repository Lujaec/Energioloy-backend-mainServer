package com.example.backendmainserver.history.domain;

import com.example.backendmainserver.port.domain.Port;
import com.example.backendmainserver.port.domain.PowerSupplier;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class PowerSupplierHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    LocalDateTime time;
    @ManyToOne
    @JoinColumn(name = "port_id")
    Port port;

    @Enumerated(EnumType.STRING)
    PowerSupplier beforePowerSupplier;
    @Enumerated(EnumType.STRING)
    PowerSupplier afterPowerSupplier;
    String description;

    @Builder
    public PowerSupplierHistory(LocalDateTime time, Port port, PowerSupplier beforePowerSupplier, PowerSupplier afterPowerSupplier, String description) {
        this.time = time;
        this.port = port;
        this.beforePowerSupplier = beforePowerSupplier;
        this.afterPowerSupplier = afterPowerSupplier;
        this.description = description;
    }
}
