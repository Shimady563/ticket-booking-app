package com.shimady.ticketbookingapp.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@AllArgsConstructor
@Getter
public class TicketsResponse {
    private final Long id;
    private int overallPrice;
    private final LocalDateTime departureTime;
    private final LocalDateTime arrivalTime;
    private final String departureCity;
    private final String arrivalCity;
    private final LocalTime estimatedTime;
}
