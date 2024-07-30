package com.example.air_pollution_app.util;

import com.example.air_pollution_app.model.PollutionData;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PollutionDataWrapper
{
    private PollutionData data;
    private String source; // "DB" or "API"

    public PollutionDataWrapper(PollutionData data, String source) {
        this.data = data;
        this.source = source;
    }

}
