package com.company.companyapp.controller;

import com.company.companyapp.error.ErrorResponse;
import com.company.companyapp.exception.CompanyNotFoundException;
import com.mongodb.MongoWriteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CompanyNotFoundException.class)
    public ResponseEntity<?> handleProfileNotFoundException(CompanyNotFoundException exception){
        ErrorResponse profileNotFound = new ErrorResponse
                (LocalDateTime.now(), exception.getMessage(), "Company Not Found");
        return new ResponseEntity<>(profileNotFound, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(MongoWriteException.class)
    public ResponseEntity<ErrorResponse> handleMongoWriteException(MongoWriteException exception) {
        if (exception.getError().getCode() == 11000) {
            // Handle duplicate key error (code 11000)
            ErrorResponse duplicateKeyError = new ErrorResponse(
                    LocalDateTime.now(),
                    "A contact with this owner and contact pair already exists.",
                    "Duplicate Key"
            );
            return new ResponseEntity<>(duplicateKeyError, HttpStatus.CONFLICT);
        }
        // Handle other MongoWriteException cases if needed
        ErrorResponse genericError = new ErrorResponse(
                LocalDateTime.now(),
                exception.getMessage(),
                "Write Error"
        );
        return new ResponseEntity<>(genericError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}