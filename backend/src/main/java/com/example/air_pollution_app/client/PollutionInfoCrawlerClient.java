package com.example.air_pollution_app.client;

import com.example.air_pollution_app.config.ApiProperties;
import com.example.air_pollution_app.dto.ApiResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.*;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PollutionInfoCrawlerClient implements IPollutionInfoCrawlerClient
{
    private final ApiProperties apiProperties;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public static final String API_POLLUTION_INFO_URL = "http://api.openweathermap.org/data/2.5/air_pollution/history?";

    public List<ApiResponseDto.PollutionDataDto> fetchPollutionInfo(double latitude, double longitude, LocalDate startDate, LocalDate endDate)
    {
        String url = formatRequestUrl(latitude, longitude, startDate, endDate);
        log.info("Request URL: {}", url);

        try
        {
            ApiResponseDto apiResponseDto = restTemplate.getForObject(url, ApiResponseDto.class);

            if (apiResponseDto != null && apiResponseDto.getPollutionDataList() != null)
            {
                log.info("Pollution Data List Size: {}", apiResponseDto.getPollutionDataList().size());
                return apiResponseDto.getPollutionDataList();
            }
            else
            {
                log.warn("No pollution data returned or responseDto is null");
                return null;
            }
        }
        catch (Exception e)
        {
            log.error("Error fetching pollution data", e);
        }

        return null;
    }

    public String formatRequestUrl(double latitude, double longitude, LocalDate startDate, LocalDate endDate)
    {
        Long unixStart = convertToUnixStartTime(startDate);
        Long unixEnd = convertToUnixEndTime(endDate);
        return API_POLLUTION_INFO_URL + "lat=" + latitude
                + "&lon=" + longitude
                + "&start=" + unixStart
                + "&end=" + unixEnd
                + "&appid=" + apiProperties.getKey();
    }

    public Long convertToUnixStartTime(LocalDate start)
    {
        ZonedDateTime startDateTime = start.atStartOfDay(ZoneOffset.UTC);
        return startDateTime.toEpochSecond();
    }

    public Long convertToUnixEndTime(LocalDate end)
    {
        ZonedDateTime endDateTime = end.plusDays(1).atStartOfDay(ZoneOffset.UTC).minusSeconds(1);
        return endDateTime.toEpochSecond();
    }

}
