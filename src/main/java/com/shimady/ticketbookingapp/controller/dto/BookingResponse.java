package com.shimady.ticketbookingapp.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Set;

@Schema(description = "Response entity for getting booking information")
public record BookingResponse(
        @Schema(example = "2024-08-24T22:06:43")
        LocalDateTime creationDate,

        @Schema(description = "List with information about booked seats")
        Set<SeatResponse> seats,

        @Schema(description = "List with information about passengers")
        Set<PassengerResponse> passengers
) {
}
