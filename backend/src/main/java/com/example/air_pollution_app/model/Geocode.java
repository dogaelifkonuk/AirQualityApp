package com.example.air_pollution_app.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "geocodes")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Geocode
{
    @Id
    @Column(name = "city")
    private String city;

    @Column(name = "lat")
    private double lat;

    @Column(name = "lon")
    private double lon;
}
