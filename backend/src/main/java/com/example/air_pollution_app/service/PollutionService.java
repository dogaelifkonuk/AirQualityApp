package com.example.air_pollution_app.service;

import com.example.air_pollution_app.client.IGeocodeCrawlerClient;
import com.example.air_pollution_app.client.IPollutionInfoCrawlerClient;
import com.example.air_pollution_app.dto.ApiResponseDto;
import com.example.air_pollution_app.dto.ApiGeocodeDto;
import com.example.air_pollution_app.dto.PollutionResponseDto;
import com.example.air_pollution_app.exception.DateOutOfRangeException;
import com.example.air_pollution_app.exception.InvalidDateRangeException;
import com.example.air_pollution_app.model.PollutionData;
import com.example.air_pollution_app.repository.IPollutionDataRepository;
import com.example.air_pollution_app.util.DateConversion;
import com.example.air_pollution_app.model.Geocode;
import com.example.air_pollution_app.repository.IGeocodeRepository;
import com.example.air_pollution_app.util.DateInterval;
import com.example.air_pollution_app.util.PollutionDataWrapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PollutionService implements IPollutionService
{

    @Autowired
    private IGeocodeRepository geocodeRepository;

    @Autowired
    private IPollutionDataRepository pollutionDataRepository;

    @Autowired
    private IGeocodeCrawlerClient geocodeCrawlerClient;

    @Autowired
    private IPollutionInfoCrawlerClient pollutionInfoCrawlerClient;

    @Override
    public Geocode findGeocode(String city)
    {

        Geocode geocode = geocodeRepository.findByCity(city);
        if(geocode != null)//geocode is already in database
        {
            return geocode;
        }

        ApiGeocodeDto apiGeocodeDto = geocodeCrawlerClient.fetchGeocode(city);

        return saveGeocode(apiGeocodeDto);
    }

    @Override
    public Geocode saveGeocode(ApiGeocodeDto apiGeocodeDto)
    {
        Geocode newGeocode = apiGeocodeDto.convertToGeocode();
        geocodeRepository.save(newGeocode);
        return newGeocode;
    }

    @Override
    public List<PollutionDataWrapper> wrapWithDatabaseLog(List<PollutionData> pollutionDataInDatabase)
    {
        List<PollutionDataWrapper> results = new ArrayList<>();
        for (PollutionData data : pollutionDataInDatabase)
        {
            results.add(new PollutionDataWrapper(data, "DB"));
        }
        return results;
    }

    @Override
    public PollutionResponseDto findPollutionInfo(String city, LocalDate startDate, LocalDate endDate)
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
            startDate = earliestDate;
        }
        else if (endDate == null) //only start date is entered
        {
            endDate = today;
        }

        if (startDate.isBefore(earliestDate) || endDate.isAfter(today))
        {
            throw new DateOutOfRangeException("Dates must be between November 27, 2020, and today.");
        }

        if (startDate.isAfter(endDate))
        {
            throw new InvalidDateRangeException("Start date must be before or equal to end date.");
        }

        Geocode geocode = findGeocode(city);

        List<PollutionData> pollutionDataInDatabase = pollutionDataRepository.findByCityAndDateBetween(city, startDate, endDate);
        List<PollutionDataWrapper> results = wrapWithDatabaseLog(pollutionDataInDatabase);

        List<DateInterval> dateIntervals = findMissingDateIntervals(pollutionDataInDatabase, startDate, endDate);

        for(DateInterval dateInterval : dateIntervals)
        {
            List<ApiResponseDto.PollutionDataDto> pollutionDataDtoList = pollutionInfoCrawlerClient.fetchPollutionInfo(geocode.getLat(), geocode.getLon(), dateInterval.getStart(), dateInterval.getEnd());

            List<PollutionData> newPollutionData = processAndSaveData(city, pollutionDataDtoList);

            for (PollutionData data : newPollutionData)
            {
                results.add(new PollutionDataWrapper(data, "API"));
            }
        }

        // Sort the result list by date and log
        results.sort(Comparator.comparing(wrapper -> wrapper.getData().getDate()));
        logDataSource(results);

        // Extract the PollutionData from the wrapper to create the response
        List<PollutionData> finalDataList = results.stream()
                .map(PollutionDataWrapper::getData)
                .collect(Collectors.toList());

        return new PollutionResponseDto(city, finalDataList);
    }

    @Override
    public List<PollutionData> processAndSaveData(String city, List<ApiResponseDto.PollutionDataDto> hourlyData)
    {
        List<PollutionData> newDataConvertedToEntity = new ArrayList<>();

        // Group data by date
        Map<LocalDate, List<ApiResponseDto.PollutionDataDto>> dataByDate = hourlyData.stream()
                .collect(Collectors.groupingBy(data -> DateConversion.convertUnixToLocalDate(data.getDt())));

        // Process each date
        for (Map.Entry<LocalDate, List<ApiResponseDto.PollutionDataDto>> entry : dataByDate.entrySet())
        {
            LocalDate date = entry.getKey();
            List<ApiResponseDto.PollutionDataDto> dataForDate = entry.getValue();

            PollutionData pollutionData = calculateAndSaveAverage(city, dataForDate, date);
            newDataConvertedToEntity.add(pollutionData);
        }
        return newDataConvertedToEntity;
    }

    public PollutionData calculateAndSaveAverage(String city, List<ApiResponseDto.PollutionDataDto> hourlyData, LocalDate date)
    {
        double totalCo = 0;
        double totalO3 = 0;
        double totalSo2 = 0;
        int count = hourlyData.size();

        // Calculate the total for each chemical
        for (ApiResponseDto.PollutionDataDto data : hourlyData)
        {
            totalCo += data.getComponents().getCo();
            totalO3 += data.getComponents().getO3();
            totalSo2 += data.getComponents().getSo2();
        }

        // Calculate the average for each chemical
        double averageCo = totalCo / count;
        double averageO3 = totalO3 / count;
        double averageSo2 = totalSo2 / count;

        // Create a new entity to save
        return savePollutionData(city, date, averageCo, averageO3, averageSo2);
    }

    @Override
    public PollutionData savePollutionData(String city, LocalDate localDate, double co, double o3, double so2)
    {
        PollutionData pollutionData = new PollutionData();
        pollutionData.setCity(city);
        pollutionData.setDate(localDate);
        pollutionData.setCo(co);
        pollutionData.setO3(o3);
        pollutionData.setSo2(so2);
        pollutionDataRepository.save(pollutionData);
        return pollutionData;
    }

    @Override
    public List<DateInterval> findMissingDateIntervals(List<PollutionData> pollutionDataList, LocalDate startDate, LocalDate endDate)
    {

        List<DateInterval> dateIntervals = new ArrayList<>();

        // Convert the list of data to a list of dates
        List<LocalDate> datesInDb = new ArrayList<>();
        for (PollutionData data : pollutionDataList)
        {
            datesInDb.add(data.getDate());
        }

        LocalDate current = startDate;
        LocalDate intervalStart = null;

        while (!current.isAfter(endDate))
        {
            if (!datesInDb.contains(current))
            {
                if (intervalStart == null)
                {
                    intervalStart = current;
                }
            }
            else
            {
                if (intervalStart != null)
                {
                    dateIntervals.add(new DateInterval(intervalStart, current.minusDays(1)));
                    intervalStart = null;
                }
            }
            current = current.plusDays(1);
        }

        // Handle the case where the interval extends to the end date
        if (intervalStart != null)
        {
            dateIntervals.add(new DateInterval(intervalStart, endDate));
        }
        return dateIntervals;
    }

    @Override
    public void logDataSource(List<PollutionDataWrapper> list)
    {
        for (PollutionDataWrapper entry : list)
        {
            log.info("Date: " + entry.getData().getDate() + ", Source: " + entry.getSource());
        }
    }

    @Override
    @Transactional
    public int deletePollutionInfo(String city, LocalDate startDate, LocalDate endDate)
    {
        return pollutionDataRepository.deleteByCityAndDateBetween(city, startDate, endDate);
    }
}