package com.example.backendmainserver.port.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Embeddable
@Getter
@AllArgsConstructor
public class BatterySwitchOption {
    @Enumerated(EnumType.STRING)
    BatterySwitchOptionType batterySwitchOptionType;

    String optionConfiguration;

    public BatterySwitchOption() {

    }
}
