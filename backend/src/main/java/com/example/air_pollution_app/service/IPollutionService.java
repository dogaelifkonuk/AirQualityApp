package com.example.air_pollution_app.service;

import com.example.air_pollution_app.dto.ApiResponseDto;
import com.example.air_pollution_app.dto.ApiGeocodeDto;
import com.example.air_pollution_app.dto.PollutionResponseDto;
import com.example.air_pollution_app.model.PollutionData;
import com.example.air_pollution_app.model.Geocode;
import com.example.air_pollution_app.util.DateInterval;
import com.example.air_pollution_app.util.PollutionDataWrapper;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface IPollutionService
{
    Geocode findGeocode(String city);
    Geocode saveGeocode(ApiGeocodeDto apiGeocodeDto);

    PollutionResponseDto findPollutionInfo(String city, LocalDate startDate, LocalDate endDate);
    List<PollutionData> processAndSaveData(String city, List<ApiResponseDto.PollutionDataDto> hourlyData);

    PollutionData calculateAndSaveAverage(String city, List<ApiResponseDto.PollutionDataDto> hourlyData, LocalDate date);

    PollutionData savePollutionData(String city, LocalDate localDate, double co, double o3, double so2);

    List<DateInterval> findMissingDateIntervals(List<PollutionData> pollutionDataList, LocalDate startDate, LocalDate endDate);

    List<PollutionDataWrapper> wrapWithDatabaseLog(List<PollutionData> pollutionDataInDatabase);

    void logDataSource(List<PollutionDataWrapper> list);

    int deletePollutionInfo(String city, LocalDate startDate, LocalDate endDate);

}
