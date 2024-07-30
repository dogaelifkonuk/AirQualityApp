package com.example.air_pollution_app.controller;

import com.example.air_pollution_app.dto.PollutionResponseDto;
import com.example.air_pollution_app.exception.DateOutOfRangeException;
import com.example.air_pollution_app.exception.InvalidDateRangeException;
import com.example.air_pollution_app.model.Geocode;
import com.example.air_pollution_app.service.IPollutionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("api/pollution")
@RequiredArgsConstructor
@Tag(name = "Air Pollution")
public class PollutionController
{
    private final IPollutionService pollutionService;

    @Operation(
            description = "Get endpoint for geocodes",
            summary = "Returns geocodes (latitude and longitude) of a specified city.",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    )
            }
    )
    @GetMapping("/geocode")
    public ResponseEntity<Geocode> getGeocode(@RequestParam (value = "city", required = true)
                                              @Parameter(description = "City name", example = "Ankara") String city)
    {
        return ResponseEntity.ok(pollutionService.findGeocode(city));
    }

    @Operation(
            description = "Get endpoint for pollution information",
            summary = "Returns daily pollution info of a specified city with classifications of CO, O3 and SO2 chemical concentrations.",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Bad Request",
                            responseCode = "400"
                    )
            }
    )
    @GetMapping("/air-quality-data")
    public ResponseEntity<PollutionResponseDto> getPollutionInfo(@RequestParam (value = "city", required = true)
                                                                    @Parameter(description = "City name", example = "Ankara") String city,
                                                                 @RequestParam(value = "startDate", required = false)
                                                                    @Parameter(description = "Start date of the pollution data range", example = "2024-01-01") LocalDate startDate,
                                                                 @RequestParam(value = "endDate", required = false)
                                                                     @Parameter(description = "End date of the pollution data range", example = "2024-01-03") LocalDate endDate)
    {
        PollutionResponseDto response = (pollutionService.findPollutionInfo(city, startDate, endDate));
        return ResponseEntity.ok(response);
    }

    @Operation(
            description = "Delete endpoint for pollution information",
            summary = "Returns number of pollution data records deleted from the database.",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    )
            }
    )
    @DeleteMapping("/air-quality-data")
    public ResponseEntity<String> deletePollutionInfo(@RequestParam (value = "city", required = true)
                                                          @Parameter(description = "City name", example = "London") String city,
                                                      @RequestParam(value = "startDate", required = false)
                                                          @Parameter(description = "Start date of the pollution data range to delete", example = "2024-03-15") LocalDate startDate,
                                                      @RequestParam(value = "endDate", required = false)
                                                          @Parameter(description = "End date of the pollution data range to delete", example = "2024-03-20") LocalDate endDate)
    {
        LocalDate today = LocalDate.now();
        LocalDate earliestDate = LocalDate.of(2020, 11, 27);


        if (startDate == null && endDate == null)
        {
            startDate = today.minusDays(7);
            endDate = today;
        }
        else if (startDate == null) //only end date is entered
        {
            startDate = endDate;
        }
        else if (endDate == null) //only start date is entered
        {
            endDate = startDate;
        }

        if (startDate.isBefore(earliestDate) || endDate.isAfter(today))
        {
            throw new DateOutOfRangeException("Dates must be between November 27, 2020, and today.");
        }

        if (startDate.isAfter(endDate))
        {
            throw new InvalidDateRangeException("Start date must be before or equal to end date.");
        }

        int deletedCount = pollutionService.deletePollutionInfo(city, startDate, endDate);
        if (deletedCount == 0)
        {
            return ResponseEntity.ok("The records specified don't exist in the database");
        }
        else
        {
            return ResponseEntity.ok(deletedCount + " records were deleted.");
        }
    }
}
