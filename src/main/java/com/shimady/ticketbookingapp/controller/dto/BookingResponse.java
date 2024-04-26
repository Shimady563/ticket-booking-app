package com.shimady.ticketbookingapp.controller.dto;

import java.time.LocalDateTime;
import java.util.Set;

public record BookingResponse(
        LocalDateTime creationDate,
        Set<SeatsResponse> seats,
        Set<PassengersResponse> passengers
) {
}
