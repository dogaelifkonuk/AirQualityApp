package com.example.air_pollution_app.dto;

import com.example.air_pollution_app.model.Geocode;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiGeocodeDto
{

    @JsonProperty("name")
    private String city;

    private double lat;

    private double lon;

    public Geocode convertToGeocode()
    {
        Geocode newGeocode = new Geocode();
        newGeocode.setCity(this.city);
        newGeocode.setLat(this.lat);
        newGeocode.setLon(this.lon);
        return newGeocode;
    }
}
