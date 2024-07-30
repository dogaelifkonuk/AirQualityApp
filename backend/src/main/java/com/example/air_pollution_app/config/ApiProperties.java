package com.example.air_pollution_app.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Getter
@Configuration
@Component
@AllArgsConstructor
@NoArgsConstructor
public class ApiProperties
{
    @Value("${api-key}")
    private String apiKey;

    public String getKey()
    {
        return apiKey;
    }
}
