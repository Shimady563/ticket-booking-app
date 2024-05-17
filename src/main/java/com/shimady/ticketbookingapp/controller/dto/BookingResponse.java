package com.shimady.ticketbookingapp.controller.dto;

import java.time.LocalDateTime;
import java.util.Set;

public record BookingResponse(
        LocalDateTime creationDate,
        Set<SeatResponse> seats,
        Set<PassengerResponse> passengers
) {
}
