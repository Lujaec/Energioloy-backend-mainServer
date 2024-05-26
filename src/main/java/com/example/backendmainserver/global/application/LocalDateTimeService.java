package com.example.backendmainserver.global.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class LocalDateTimeService {
    public LocalDateTime getFormattedLocalDateTime(LocalDateTime time){
        return LocalDateTime.of(time.getYear(), time.getMonth(), time.getDayOfMonth(), time.getHour(), time.getMinute());
    }

    public LocalDateTime getBeforeOneMinuteLocalDateTime(LocalDateTime time){
        LocalDateTime formattedLocalDateTime = getFormattedLocalDateTime(time);
        return formattedLocalDateTime.minusMinutes(1);
    }
}
