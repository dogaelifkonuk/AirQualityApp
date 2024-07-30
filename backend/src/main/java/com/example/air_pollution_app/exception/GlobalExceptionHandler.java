package com.example.air_pollution_app.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(InvalidDateRangeException.class)
    @ResponseBody
    public ResponseEntity<String> handleInvalidDateRangeException(InvalidDateRangeException ex)
    {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(DateOutOfRangeException.class)
    @ResponseBody
    public ResponseEntity<String> handleDateOutOfRangeException(DateOutOfRangeException ex)
    {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

}
