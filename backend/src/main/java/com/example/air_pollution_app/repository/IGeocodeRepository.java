package com.example.air_pollution_app.repository;

import com.example.air_pollution_app.model.Geocode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface IGeocodeRepository extends JpaRepository<Geocode, Long>
{
    @Query("SELECT g FROM Geocode g WHERE g.city = :city")
    Geocode findByCity(String city);
}
