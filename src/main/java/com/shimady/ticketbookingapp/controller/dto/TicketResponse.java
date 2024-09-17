package com.shimady.ticketbookingapp.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.time.LocalTime;

//flightId field is for further tickets searching
@Schema(description = "Response entity for getting flight information")
public record TicketResponse(
        @Schema(example = "1")
        Long flightId,

        @Schema(
                description = "Approximate price of specified amount of tickets with specified seat type for that flight",
                example = "2000"
        )
        int overallPrice,

        @Schema(example = "2024-08-24T22:06:43")
        LocalDateTime departureTime,

        @Schema(example = "2024-08-25T22:06:43")
        LocalDateTime arrivalTime,

        @Schema(description = "Departure city of that flight", example = "")
        String departureCity,

        @Schema(description = "Arrival city of that flight", example = "")
        String arrivalCity,

        @Schema(description = "Flight time", example = "24-00-00")
        LocalTime estimatedTime
) {
}
