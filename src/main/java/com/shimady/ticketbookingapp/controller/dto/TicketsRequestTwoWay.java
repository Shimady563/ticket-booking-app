package com.shimady.ticketbookingapp.controller.dto;

import com.shimady.ticketbookingapp.model.SeatType;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TicketsRequestTwoWay extends TicketsRequestOneWay {
    private final LocalDate returnDepartureDate;

    public TicketsRequestTwoWay(
            String sourceAirportCode,
            String destinationAirportCode,
            LocalDate departureDate,
            SeatType seatType,
            int personCount,
            LocalDate departureTimeBack
    ) {
        super(sourceAirportCode, destinationAirportCode, departureDate, seatType, personCount);
        this.returnDepartureDate = departureTimeBack;
    }
}
