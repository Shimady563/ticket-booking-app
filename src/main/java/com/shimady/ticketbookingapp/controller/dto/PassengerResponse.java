package com.shimady.ticketbookingapp.controller.dto;

import java.time.LocalDate;

public record PassengerResponse(
        String firstName,
        String lastName,
        LocalDate birthDate,
        String citizenship,
        Long passportName,
        LocalDate passportExpiryDate
) {
}
