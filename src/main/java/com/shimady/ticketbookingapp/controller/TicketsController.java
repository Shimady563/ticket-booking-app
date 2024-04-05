package com.shimady.ticketbookingapp.controller;

import com.shimady.ticketbookingapp.service.TicketsService;
import com.shimady.ticketbookingapp.model.Flight;
import com.shimady.ticketbookingapp.model.SeatType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/search")
public class TicketsController {

    private final TicketsService ticketsService;

    @Autowired
    public TicketsController(TicketsService ticketsService) {
        this.ticketsService = ticketsService;
    }

    @PostMapping("/")
    private List<TicketsResponse> getSeatsInfo(@RequestBody TicketsRequest request) {
        List<Flight> flights = ticketsService.getFlights(
                request.sourceAirportCode,
                request.destinationAirportCode,
                request.departureDate
        );

        List<TicketsResponse> ticketsResponses = new ArrayList<>();
        for (Flight flight : flights) {
            ticketsService.getSeatByType(flight, request.seatType(), request.personCount).ifPresent(
                    seat -> ticketsResponses
                            .add(new TicketsResponse(
                                    flight.getId(),
                                    seat.getPrice(),
                                    request.seatType,
                                    flight.getDepartureTime(),
                                    flight.getArrivalTime(),
                                    flight.getSourceAirport().getCity(),
                                    flight.getDestinationAirport().getCity(),
                                    ticketsService.getEstimatedTime(flight.getDepartureTime(), flight.getArrivalTime())
                            )));

        }

        return ticketsResponses;
    }

    private record TicketsRequest(
            String sourceAirportCode,
            String destinationAirportCode,
            LocalDate departureDate,
            SeatType seatType,
            int personCount
    ) {
    }

    private record TicketsResponse(
            Long flightId,
            int seatPrice,
            SeatType seatType,
            LocalDateTime departureTime,
            LocalDateTime arrivalTime,
            String departureCity,
            String arrivalCity,
            LocalTime estimatedTime
    ) {
    }
}
