package com.example.backendmainserver.port.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Port {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "port_id")
    private Long id;

    private Long minimumOutput;

    private Long maximumOutput;
}
