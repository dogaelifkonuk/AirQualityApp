package com.example.air_pollution_app.dto;

import com.example.air_pollution_app.model.PollutionData;
import com.example.air_pollution_app.util.DateConversion;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class ApiResponseDto
{
    @JsonProperty("list")
    private List<PollutionDataDto> pollutionList;

    public List<PollutionDataDto> getPollutionDataList()
    {
        return pollutionList;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Setter
    public static class PollutionDataDto
    {
        private Long dt;
        private Components components;

        public PollutionData convertToPollutionData(String city)
        {
            PollutionData pollutionData = new PollutionData();
            pollutionData.setCity(city);
            pollutionData.setDate(DateConversion.convertUnixToLocalDate(dt));
            pollutionData.setCo(components.getCo());
            pollutionData.setO3(components.getO3());
            pollutionData.setSo2(components.getSo2());
            return pollutionData;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        @Getter
        @Setter
        public static class Components
        {
            private double co;
            private double so2;
            private double o3;
        }
    }
}
