package com.example.backendmainserver.port.domain;

import com.example.backendmainserver.power.domain.Power;

public enum PowerSupplier {
    EXTERNAL("EXTERNAL"), BATTERY("BATTERY"), OFF("OFF");

    private final String name;

    PowerSupplier(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getNameKr(){
        if(this.equals(PowerSupplier.EXTERNAL))
            return "외부전력";
        else if(this.equals(PowerSupplier.BATTERY))
            return "배터리";
        else
            return "차단";
    }
}
