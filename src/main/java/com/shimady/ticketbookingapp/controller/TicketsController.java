package com.shimady.ticketbookingapp.controller;

import com.shimady.ticketbookingapp.model.Seat;
import com.shimady.ticketbookingapp.model.SeatType;
import com.shimady.ticketbookingapp.service.TicketsService;
import com.shimady.ticketbookingapp.model.Flight;
import com.shimady.ticketbookingapp.controller.dto.TicketsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    @GetMapping("/one-way")
    public List<TicketsResponse> getOneWayTicketsInfo(
            @RequestParam String sourceAirportCode,
            @RequestParam String destinationAirportCode,
            @RequestParam LocalDate departureDate,
            @RequestParam(required = false, defaultValue = "ECONOMY") SeatType seatType,
            @RequestParam(required = false, defaultValue = "1") int personCount
    ) {
        List<Flight> flights = ticketsService.getFlights(
                sourceAirportCode,
                destinationAirportCode,
                departureDate
        );

        //returning flights that have required seats
        List<TicketsResponse> ticketsResponses = new ArrayList<>();
        for (Flight flight : flights) {
            ticketsService.getSeatByType(flight, seatType, personCount).ifPresent(
                    seat -> ticketsResponses
                            .add(new TicketsResponse(
                                    flight.getId(),
                                    seat.getPrice() * personCount,
                                    flight.getDepartureTime(),
                                    flight.getArrivalTime(),
                                    flight.getSourceAirport().getCity(),
                                    flight.getDestinationAirport().getCity(),
                                    ticketsService.getEstimatedTime(flight.getDepartureTime(), flight.getArrivalTime())
                            )));

        }

        return ticketsResponses;
    }

    @GetMapping("/two-way")
    public List<Pair<TicketsResponse, TicketsResponse>> getTwoWayTicketsInfo(
            @RequestParam String sourceAirportCode,
            @RequestParam String destinationAirportCode,
            @RequestParam LocalDate departureDate,
            @RequestParam LocalDate returnDepartureDate,
            @RequestParam(required = false, defaultValue = "ECONOMY") SeatType seatType,
            @RequestParam(required = false, defaultValue = "1") int personCount
    ) {
        List<Flight> flights = ticketsService.getFlights(
                sourceAirportCode,
                destinationAirportCode,
                departureDate
        );
        List<Flight> returnFlights = ticketsService.getFlights(
                destinationAirportCode,
                sourceAirportCode,
                returnDepartureDate
        );

        //return all matching pairs of direct and return flights
        List<Pair<TicketsResponse, TicketsResponse>> twoWayResponses = new ArrayList<>();
        for (Flight flight : flights) {
            Optional<Seat> seat = ticketsService.getSeatByType(flight, seatType, personCount);

            //the problem here is that every return flight
            //is processed inside the second loop several times
            //but I can't think of better solution in this situation for now
            for (Flight returnFlight : returnFlights) {
                Optional<Seat> returnSeat = ticketsService.getSeatByType(returnFlight, seatType, personCount);

                //test if both seats exist
                //and there are at least two hours between the flights
                if (seat.isPresent()
                        && returnSeat.isPresent()
                        && flight.getArrivalTime().plusHours(2).isBefore(returnFlight.getDepartureTime())) {

                    twoWayResponses.add(Pair.of(new TicketsResponse(
                            flight.getId(),
                            seat.get().getPrice() * personCount,
                            flight.getDepartureTime(),
                            flight.getArrivalTime(),
                            flight.getSourceAirport().getCity(),
                            flight.getDestinationAirport().getCity(),
                            ticketsService.getEstimatedTime(flight.getDepartureTime(), flight.getArrivalTime())
                    ), new TicketsResponse(
                            returnFlight.getId(),
                            returnSeat.get().getPrice() * personCount,
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
