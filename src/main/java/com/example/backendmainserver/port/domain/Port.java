package com.example.backendmainserver.port.domain;

import com.example.backendmainserver.room.domain.Room;
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

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
}
