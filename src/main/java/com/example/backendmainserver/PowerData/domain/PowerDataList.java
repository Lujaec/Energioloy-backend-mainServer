package com.example.backendmainserver.PowerData.domain;

import com.example.backendmainserver.PowerData.domain.PowerData;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PowerDataList {
    private List<PowerData> powerDataList;

    public PowerDataList() {
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("PowerDataList{\n");
        if (powerDataList != null) {
            for (PowerData pd : powerDataList) {
                sb.append("    ").append(pd.toString()).append(",\n");
            }
        }
        sb.append('}');
        return sb.toString();
    }

    @Builder
    public PowerDataList(List<PowerData> powerDataList) {
        this.powerDataList = powerDataList;
    }
}