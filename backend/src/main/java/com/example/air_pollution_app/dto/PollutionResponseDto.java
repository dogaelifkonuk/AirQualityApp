package com.example.air_pollution_app.dto;

import com.example.air_pollution_app.model.PollutionData;
import com.example.air_pollution_app.util.ComponentCategory;
import com.example.air_pollution_app.util.DateConversion;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PollutionResponseDto
{

    @JsonProperty("City") String city;

    @JsonProperty("Results") List<PollutionDataResponseDto> results;

    public PollutionResponseDto(String city, List<PollutionData> pollutionDataList)
    {
        this.city = city;
        this.results = new ArrayList<>();

        for(PollutionData pollutionData: pollutionDataList)
        {
            PollutionDataResponseDto.Categories categories = new PollutionDataResponseDto.Categories(pollutionData.getCo(), pollutionData.getSo2(), pollutionData.getO3());
            PollutionDataResponseDto pollutionDataResponseDto = new PollutionDataResponseDto(pollutionData.getDate(), categories);
            results.add(pollutionDataResponseDto);
        }
    }

    @Getter
    @Setter
    public static class PollutionDataResponseDto
    {
        @JsonProperty("Date") String date;

        @JsonProperty("Categories") Categories categories;

        public PollutionDataResponseDto(LocalDate date, Categories categories)
        {
            this.date = DateConversion.convertLocalDateToString(date);
            this.categories = categories;
        }

        @JsonPropertyOrder({"co", "so2", "o3"})
        @Getter
        @Setter
        public static class Categories
        {
            @JsonProperty("CO") String CO;
            @JsonProperty("SO2") String SO2;
            @JsonProperty("O3") String O3;

            public Categories (double CO, double SO2, double O3)
            {
                this.CO  = ComponentCategory.categorizeCO(CO);
                this.O3 = ComponentCategory.categorizeO3(O3);
                this.SO2 = ComponentCategory.categorizeSO2(SO2);
            }
        }
    }
}
