package com.shimady.ticketbookingapp.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ApplicationError {
    private LocalDateTime timestamp;
    private int statusCode;
    private String message;

    public ApplicationError(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
        this.timestamp = LocalDateTime.now();
    }
}
