package com.example.air_pollution_app.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateConversion
{
    public static LocalDate convertUnixToLocalDate(Long unixTime)
    {
        return Instant.ofEpochSecond(unixTime)
                .atZone(ZoneId.of("UTC"))
                .toLocalDate();
    }

    public static String convertLocalDateToString(LocalDate localDate)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return localDate.format(formatter);
    }
}
