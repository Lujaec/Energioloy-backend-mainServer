package com.example.backendmainserver.port.domain;

public enum LoadLevel {
    LOW("LOW"),
    MEDIUM("MEDIUM"),
    HIGH("HIGH");

    private final String name;

    LoadLevel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getKrName(){
        if(this.equals(LoadLevel.LOW))
            return "경부하";
        else if(this.equals(LoadLevel.MEDIUM))
            return "중간 부하";
        else
            return "최대 부하";
    }
}
