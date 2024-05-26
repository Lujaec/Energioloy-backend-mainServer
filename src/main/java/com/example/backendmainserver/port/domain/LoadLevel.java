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
}
