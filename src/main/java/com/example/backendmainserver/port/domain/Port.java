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

    public void validateBatterySwitchOptionType(BatterySwitchOptionType batterySwitchOptionType){
        batterySwitchOptionType = batterySwitchOption.getBatterySwitchOptionType();

        if (!(batterySwitchOptionType.equals(BatterySwitchOptionType.OPTION_TIME) ||
                batterySwitchOptionType.equals(BatterySwitchOptionType.OPTION_PREDICTION) ||
                batterySwitchOptionType.equals(BatterySwitchOptionType.OPTION_MANUAL))){
            throw new IllegalArgumentException("유효하지 않은 배터리 스위치 타입입니다.");
        }
    }

    public void validateBatterySwitchOption(BatterySwitchOption batterySwitchOption){
        BatterySwitchOptionType batterySwitchOptionType = batterySwitchOption.getBatterySwitchOptionType();

        validateBatterySwitchOptionType(batterySwitchOptionType);

        if(batterySwitchOptionType.equals(BatterySwitchOptionType.OPTION_TIME)){
            if(!isValidFormat(batterySwitchOption.optionConfiguration))
                throw new IllegalArgumentException("올바르지 않은 설정형식입니다.");
        } else if(batterySwitchOptionType.equals(BatterySwitchOptionType.OPTION_PREDICTION)){
            int num = Integer.parseInt(batterySwitchOption.getOptionConfiguration());

            if(!(0 <= num && num <= 100))
                throw new IllegalArgumentException("숫자의 설정 범위가 올바르지 않습니다 num = " + num );
        }
    }

    private boolean isValidFormat(String input) {
        String pattern = "^LOW-(EXTERNAL|BATTERY|OFF),MEDIUM-(EXTERNAL|BATTERY|OFF),HIGH-(EXTERNAL|BATTERY|OFF)$";
        return input.matches(pattern);
    }

    public void updateBatterSwitchOption(BatterySwitchOption batterySwitchOption){
        BatterySwitchOptionType batterySwitchOptionType = batterySwitchOption.getBatterySwitchOptionType();
        validateBatterySwitchOptionType(batterySwitchOptionType);
        validateBatterySwitchOption(batterySwitchOption);
        this.batterySwitchOption = batterySwitchOption;
    }
}
