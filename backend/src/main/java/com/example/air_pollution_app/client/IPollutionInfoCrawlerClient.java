package com.example.air_pollution_app.client;

import com.example.air_pollution_app.dto.ApiResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface IPollutionInfoCrawlerClient
{
    Long convertToUnixStartTime(LocalDate start);

    Long convertToUnixEndTime(LocalDate end);

    String formatRequestUrl(double latitude, double longitude, LocalDate startDate, LocalDate endDate);

    List<ApiResponseDto.PollutionDataDto> fetchPollutionInfo(double latitude, double longitude, LocalDate startDate, LocalDate endDate);

}