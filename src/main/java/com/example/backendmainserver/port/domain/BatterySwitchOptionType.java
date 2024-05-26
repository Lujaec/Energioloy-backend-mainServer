package com.example.backendmainserver.port.domain;

public enum BatterySwitchOptionType {
    OPTION_TIME("time_option"),
    OPTION_PREDICTION("prediction_option"),
    OPTION_MANUAL("manual_option");

    private String name;

    BatterySwitchOptionType(String name) {
        this.name = name;
    }
}