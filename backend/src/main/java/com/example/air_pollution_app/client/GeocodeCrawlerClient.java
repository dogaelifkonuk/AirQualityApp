package com.example.air_pollution_app.client;

import com.example.air_pollution_app.config.ApiProperties;
import com.example.air_pollution_app.dto.ApiGeocodeDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeocodeCrawlerClient implements IGeocodeCrawlerClient
{
    private final ApiProperties apiProperties;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public static final String API_GEOCODE_URL = "http://api.openweathermap.org/geo/1.0/direct?q=";

    @Override
    public ApiGeocodeDto fetchGeocode(String city)
    {
        try
        {
            String url = API_GEOCODE_URL + city + "&limit=1&appid=" + apiProperties.getKey();

            String jsonResponse = restTemplate.getForObject(url, String.class);
            ApiGeocodeDto[] apiGeocodeDtos = objectMapper.readValue(jsonResponse, ApiGeocodeDto[].class);
            return apiGeocodeDtos[0];
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
