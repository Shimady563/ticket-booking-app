package com.shimady.ticketbookingapp.controller.dto;

import com.shimady.ticketbookingapp.model.Passenger;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.Set;

@Schema(description = "Request entity for making booking")
public record BookingRequest(
        @NotEmpty(message = "id list cannot be empty")
        @Schema(description = "List of booked seats")
        List<Long> seatIds,

        @NotEmpty(message = "passenger list cannot be empty")
        @Schema(description = "List of passengers")
        Set<Passenger> passengers
) {
}
