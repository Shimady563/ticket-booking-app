package com.shimady.ticketbookingapp.controller.dto;

import com.shimady.ticketbookingapp.model.Passenger;

import java.util.List;

public record BookingRequest(
        List<Long> seatsIds,
        List<Passenger> passengers
) {
}
