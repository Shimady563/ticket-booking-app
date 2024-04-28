package com.shimady.ticketbookingapp.controller.advise;

import com.shimady.ticketbookingapp.exception.ApplicationError;
import com.shimady.ticketbookingapp.exception.BadRequestException;
import com.shimady.ticketbookingapp.exception.BookingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApplicationError> handleBadRequestException(BadRequestException e) {
        return new ResponseEntity<>(
                new ApplicationError(
                        e.getMessage(),
                        HttpStatus.BAD_REQUEST.value()
                ),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(BookingException.class)
    public ResponseEntity<ApplicationError> handleBookingException(BookingException e) {
        return new ResponseEntity<>(
                new ApplicationError(
                        e.getMessage(),
                        HttpStatus.CONFLICT.value()
                ),
                HttpStatus.CONFLICT
        );
    }
}
