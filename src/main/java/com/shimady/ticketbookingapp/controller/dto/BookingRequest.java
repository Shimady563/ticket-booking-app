package com.shimady.ticketbookingapp.controller.dto;

import com.shimady.ticketbookingapp.model.Passenger;

import java.util.List;
import java.util.Set;

public record BookingRequest(
        List<Long> seatIds,
        Set<Passenger> passengers
) {
}
