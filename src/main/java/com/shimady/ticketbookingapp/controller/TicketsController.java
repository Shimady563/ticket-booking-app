package com.shimady.ticketbookingapp.controller;

import com.shimady.ticketbookingapp.model.Seat;
import com.shimady.ticketbookingapp.service.TicketsService;
import com.shimady.ticketbookingapp.model.Flight;
import com.shimady.ticketbookingapp.controller.dto.TicketsRequestOneWay;
import com.shimady.ticketbookingapp.controller.dto.TicketsRequestTwoWay;
import com.shimady.ticketbookingapp.controller.dto.TicketsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/search")
public class TicketsController {

    private final TicketsService ticketsService;

    @Autowired
    public TicketsController(TicketsService ticketsService) {
        this.ticketsService = ticketsService;
    }

    @PostMapping("/one-way")
    public List<TicketsResponse> getOneWaySeatsInfo(@RequestBody TicketsRequestOneWay request) {
        List<Flight> flights = ticketsService.getFlights(
                request.getSourceAirportCode(),
                request.getDestinationAirportCode(),
                request.getDepartureDate()
        );

        System.out.println(flights);

        List<TicketsResponse> ticketsResponses = new ArrayList<>();
        for (Flight flight : flights) {
            ticketsService.getSeatByType(flight, request.getSeatType(), request.getPersonCount()).ifPresent(
                    seat -> ticketsResponses
                            .add(new TicketsResponse(
                                    flight.getId(),
                                    seat.getPrice() * request.getPersonCount(),
                                    flight.getDepartureTime(),
                                    flight.getArrivalTime(),
                                    flight.getSourceAirport().getCity(),
                                    flight.getDestinationAirport().getCity(),
                                    ticketsService.getEstimatedTime(flight.getDepartureTime(), flight.getArrivalTime())
                            )));

        }

        return ticketsResponses;
    }

    @PostMapping("/two-way")
    public List<Pair<TicketsResponse, TicketsResponse>> getTwoWaySeatsInfo(@RequestBody TicketsRequestTwoWay request) {
        List<Flight> flights = ticketsService.getFlights(
                request.getSourceAirportCode(),
                request.getDestinationAirportCode(),
                request.getDepartureDate()
        );
        List<Flight> returnFlights = ticketsService.getFlights(
                request.getDestinationAirportCode(),
                request.getSourceAirportCode(),
                request.getReturnDepartureDate()
        );

        List<Pair<TicketsResponse, TicketsResponse>> twoWayResponses = new ArrayList<>();
        for (Flight flight : flights) {
            for (Flight returnFlight : returnFlights) {
                Optional<Seat> seat = ticketsService.getSeatByType(flight, request.getSeatType(), request.getPersonCount());
                Optional<Seat> returnSeat = ticketsService.getSeatByType(returnFlight, request.getSeatType(), request.getPersonCount());
                if (seat.isPresent() && returnSeat.isPresent()) {
                    twoWayResponses.add(Pair.of(
                            new TicketsResponse(
                                    flight.getId(),
                                    seat.get().getPrice() * request.getPersonCount(),
                                    flight.getDepartureTime(),
                                    flight.getArrivalTime(),
                                    flight.getSourceAirport().getCity(),
                                    flight.getDestinationAirport().getCity(),
                                    ticketsService.getEstimatedTime(flight.getDepartureTime(), flight.getArrivalTime())
                            ), new TicketsResponse(
                                    returnFlight.getId(),
                                    returnSeat.get().getPrice() * request.getPersonCount(),
                                    returnFlight.getDepartureTime(),
                                    returnFlight.getArrivalTime(),
                                    returnFlight.getSourceAirport().getCity(),
                                    returnFlight.getDestinationAirport().getCity(),
                                    ticketsService.getEstimatedTime(returnFlight.getDepartureTime(), returnFlight.getArrivalTime())
                            )));
                }
            }
        }

        return twoWayResponses;
    }
}
