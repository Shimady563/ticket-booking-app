package com.shimady.ticketbookingapp.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Response entity for getting passenger information")
public record PassengerResponse(
        @Schema(example = "Ivan")
        String firstName,

        @Schema(example = "Ivanov")
        String lastName,

        @Schema(example = "1987-08-24")
        LocalDate birthDate,

        @Schema(example = "Russia")
        String citizenship,

        @Schema(example = "123456")
        Long passportNumber,

        @Schema(example = "2026-08-24")
        LocalDate passportExpiryDate
) {
}
