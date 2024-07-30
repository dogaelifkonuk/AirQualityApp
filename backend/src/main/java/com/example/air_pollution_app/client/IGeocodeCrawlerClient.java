package com.example.air_pollution_app.client;

import com.example.air_pollution_app.dto.ApiGeocodeDto;

public interface IGeocodeCrawlerClient
{
    ApiGeocodeDto fetchGeocode(String city);
}
