package com.shimady.ticketbookingapp.controller.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;

//flightId field is for further tickets searching
public record TicketsResponse(Long flightId, int overallPrice, LocalDateTime departureTime, LocalDateTime arrivalTime,
                              String departureCity, String arrivalCity, LocalTime estimatedTime) {
}
