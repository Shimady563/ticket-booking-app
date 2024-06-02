package com.shimady.ticketbookingapp.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ValidationError {

    private List<Violation> violations;
    private int statusCode;

    @Getter
    @AllArgsConstructor
    public static class Violation {
        private String field;
        private String message;
    }
}
