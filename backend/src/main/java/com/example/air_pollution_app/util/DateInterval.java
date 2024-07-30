package com.example.air_pollution_app.util;

import lombok.Getter;

import java.time.LocalDate;

public class DateInterval
{
    @Getter
    private LocalDate start;
    @Getter
    private LocalDate end;

    public DateInterval(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }
}
