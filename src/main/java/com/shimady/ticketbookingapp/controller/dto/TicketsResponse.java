package com.shimady.ticketbookingapp.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@AllArgsConstructor
@Getter
public class TicketsResponse {
    //returning flight id for further tickets searching
    private final Long flightId;
    private int overallPrice;
    private final LocalDateTime departureTime;
    private final LocalDateTime arrivalTime;
    private final String departureCity;
    private final String arrivalCity;
    private final LocalTime estimatedTime;
}
