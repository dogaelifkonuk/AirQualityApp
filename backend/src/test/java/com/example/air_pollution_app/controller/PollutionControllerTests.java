package com.example.air_pollution_app.controller;

import com.example.air_pollution_app.dto.PollutionResponseDto;
import com.example.air_pollution_app.exception.DateOutOfRangeException;
import com.example.air_pollution_app.model.Geocode;
import com.example.air_pollution_app.model.PollutionData;
import com.example.air_pollution_app.service.IPollutionService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PollutionController.class)
@ContextConfiguration(classes = PollutionController.class)
public class PollutionControllerTests extends ControllerTestsBase
{
    @MockBean
    private IPollutionService pollutionService;


    @Test
    public void testGetGeocode() throws Exception
    {
        Geocode mockGeocode = new Geocode();
        mockGeocode.setCity("Paris");
        mockGeocode.setLat(48.8588897);
        mockGeocode.setLon(2.3200410217200766);

        given(pollutionService.findGeocode(anyString())).willReturn(mockGeocode);

        mockMvc.perform(get("/api/pollution/geocode")
                        .param("city", "Paris")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"city\":\"Paris\",\"lat\":48.8588897,\"lon\":2.3200410217200766}"));
    }

    @Test
    public void testGetPollutionInfo() throws Exception
    {
        PollutionData pollutionData1 = new PollutionData();
        pollutionData1.setCity("Mumbai");
        pollutionData1.setDate(LocalDate.of(2022, 1, 1));
        pollutionData1.setCo(1797.437916666666);
        pollutionData1.setSo2(43.60041666666666);
        pollutionData1.setO3(102.4175);

        PollutionData pollutionData2 = new PollutionData();
        pollutionData2.setCity("Mumbai");
        pollutionData2.setDate(LocalDate.of(2022, 1, 2));
        pollutionData2.setCo(2036.6508333333334);
        pollutionData2.setSo2(55.095000000000006);
        pollutionData2.setO3(114.14541666666668);

        PollutionData pollutionData3 = new PollutionData();
        pollutionData3.setCity("Mumbai");
        pollutionData3.setDate(LocalDate.of(2022, 1, 3));
        pollutionData3.setCo(3020.763333333333);
        pollutionData3.setSo2(67.3925);
        pollutionData3.setO3(92.85874999999999);

        List<PollutionData> mockPollutionDataList = Arrays.asList(pollutionData1, pollutionData2, pollutionData3);

        PollutionResponseDto mockResponseDto = new PollutionResponseDto("Mumbai", mockPollutionDataList);

        given(pollutionService.findPollutionInfo(anyString(), any(LocalDate.class), any(LocalDate.class)))
                .willReturn(mockResponseDto);

        mockMvc.perform(get("/api/pollution/air-quality-data")
                        .param("city", "Mumbai")
                        .param("startDate", "2022-01-01")
                        .param("endDate", "2022-01-03")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"City\":\"Mumbai\",\"Results\":[{\"Date\":\"01-01-2022\",\"Categories\":{\"CO\":\"Satisfactory\",\"SO2\":\"Satisfactory\",\"O3\":\"Moderate\"}},{\"Date\":\"02-01-2022\",\"Categories\":{\"CO\":\"Moderate\",\"SO2\":\"Satisfactory\",\"O3\":\"Moderate\"}},{\"Date\":\"03-01-2022\",\"Categories\":{\"CO\":\"Moderate\",\"SO2\":\"Satisfactory\",\"O3\":\"Satisfactory\"}}]}"));

    }

    /*
    @Test
    public void testStartDateBeforeEarliestDate() throws Exception
    {

        Exception exception = assertThrows(DateOutOfRangeException.class, () -> {
            mockMvc.perform(get("/api/pollution/air-quality-data")
                    .param("city", "Ankara")
                    .param("startDate", "2018-11-26") // Start date before earliestDate
                    .param("endDate", "2022-01-01")
                    .contentType(MediaType.APPLICATION_JSON)).andExpect();

        });

        String expectedMessage = "Dates must be between November 27, 2020, and today.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    public void testEndDateAfterToday() throws Exception {
        LocalDate currentDate = LocalDate.now();
        LocalDate futureDate = currentDate.plusDays(5);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/pollution/air-quality-data")
                        .param("city", "London")
                        .param("startDate", "2021-01-01")
                        .param("endDate", String.valueOf(futureDate)) // End date after today
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Dates must be between November 27, 2020, and today."));
    }

    @Test
    public void testStartDateAfterEndDate() throws Exception
    {
        LocalDate startDate = LocalDate.of(2022, 1, 1);
        LocalDate endDate = startDate.minusDays(5);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/pollution/air-quality-data")
                        .param("city", "Tokyo")
                        .param("startDate", String.valueOf(startDate))
                        .param("endDate", String.valueOf(endDate)) // End date after today
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Start date must be before or equal to end date."));
    }
    */

}
