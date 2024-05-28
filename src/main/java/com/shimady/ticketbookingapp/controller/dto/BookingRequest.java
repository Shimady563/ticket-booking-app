package com.shimady.ticketbookingapp.controller.dto;

import com.shimady.ticketbookingapp.model.Passenger;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.Set;

public record BookingRequest(
        @NotEmpty(message = "id list cannot be empty")
        List<Long> seatIds,
        @NotEmpty(message = "passenger list cannot be empty")
        Set<Passenger> passengers
) {
}
