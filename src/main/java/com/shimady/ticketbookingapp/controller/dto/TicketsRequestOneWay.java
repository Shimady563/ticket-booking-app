package com.shimady.ticketbookingapp.controller.dto;

import com.shimady.ticketbookingapp.model.SeatType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class TicketsRequestOneWay {
    private final String sourceAirportCode;
    private final String destinationAirportCode;
    private final LocalDate departureDate;
    private final SeatType seatType;
    private final int personCount;
}