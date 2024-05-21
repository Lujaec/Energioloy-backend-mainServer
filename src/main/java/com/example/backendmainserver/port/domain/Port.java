package com.example.backendmainserver.port.domain;

import com.example.backendmainserver.room.domain.Room;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
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

    @Embedded
    private BatterySwitchOption batterySwitchOption;

    @Enumerated(EnumType.STRING)
    private PowerSupplier powerSupplier;

    @Builder
    public Port(Long id, Long minimumOutput, Long maximumOutput, Room room, BatterySwitchOption batterySwitchOption, PowerSupplier powerSupplier) {
        this.id = id;
        this.minimumOutput = minimumOutput;
        this.maximumOutput = maximumOutput;
        this.room = room;
        this.batterySwitchOption = batterySwitchOption;
        this.powerSupplier = powerSupplier;
    }

    public void validateBatterySwitchOption(){
        BatterySwitchOptionType batterySwitchOptionType = batterySwitchOption.getBatterySwitchOptionType();

        if (!(batterySwitchOptionType.equals(BatterySwitchOptionType.OPTION_TIME) ||
                batterySwitchOptionType.equals(BatterySwitchOptionType.OPTION_PREDICTION))){
            throw new IllegalArgumentException("유효하지 않은 배터리 스위치 타입입니다.");
        }
    }

}
