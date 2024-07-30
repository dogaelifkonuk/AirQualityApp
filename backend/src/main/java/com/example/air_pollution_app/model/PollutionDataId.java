package com.example.air_pollution_app.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class PollutionDataId implements Serializable
{
    private String city;
    private LocalDate date;

    public PollutionDataId() {}

    public PollutionDataId(String city, LocalDate date)
    {
        this.city = city;
        this.date = date;
    }
    @Override
    public int hashCode()
    {
        return Objects.hash(city, date);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PollutionDataId that = (PollutionDataId) o;
        return Objects.equals(city, that.city) && Objects.equals(date, that.date);
    }
}
