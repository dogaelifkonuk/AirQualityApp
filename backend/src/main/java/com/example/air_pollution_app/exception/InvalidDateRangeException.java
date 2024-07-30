package com.example.air_pollution_app.exception;


public class InvalidDateRangeException extends RuntimeException
{
    public InvalidDateRangeException(String message)
    {
        super(message);
    }
}