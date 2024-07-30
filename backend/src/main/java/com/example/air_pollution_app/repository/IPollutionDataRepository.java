package com.example.air_pollution_app.repository;

import com.example.air_pollution_app.model.PollutionData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface IPollutionDataRepository extends JpaRepository<PollutionData, Long>
{
    @Query("SELECT d FROM PollutionData d WHERE d.city = :city AND d.date = :date")
    PollutionData findByCityDaily(String city, LocalDate date);

    @Query("SELECT d FROM PollutionData d WHERE d.city = :city AND d.date BETWEEN :startDate AND :endDate ORDER BY d.date ASC")
    List<PollutionData> findByCityAndDateBetween(String city, LocalDate startDate, LocalDate endDate);

    @Modifying
    @Query("DELETE FROM PollutionData d WHERE d.city = :city AND d.date BETWEEN :startDate AND :endDate")
    int deleteByCityAndDateBetween(String city, LocalDate startDate, LocalDate endDate);

}
