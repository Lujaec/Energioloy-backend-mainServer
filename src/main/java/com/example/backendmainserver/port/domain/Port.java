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

    @Embedded
    private BatterySwitchOption batterySwitchOption;

    public void validateBatterySwitchOption(){
        BatterySwitchOptionType batterySwitchOptionType = batterySwitchOption.getBatterySwitchOptionType();

        if (!(batterySwitchOptionType.equals(BatterySwitchOptionType.OPTION_TIME) ||
                batterySwitchOptionType.equals(BatterySwitchOptionType.OPTION_PREDICTION))){
            throw new IllegalArgumentException("유효하지 않은 배터리 스위치 타입입니다.");
        }
    }

}
