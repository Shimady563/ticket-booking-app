package com.shimady.ticketbookingapp.service;

import com.shimady.ticketbookingapp.controller.dto.TicketsResponse;
import com.shimady.ticketbookingapp.model.Flight;
import com.shimady.ticketbookingapp.model.Seat;
import com.shimady.ticketbookingapp.model.SeatType;
import com.shimady.ticketbookingapp.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TicketsService {

    private final FlightRepository flightRepository;

    @Autowired
    public TicketsService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Transactional(readOnly = true)
    public List<Flight> getFlightsByAirports(
            String sourceAirportCode,
            String destinationAirportCode,
            LocalDate departureDate
    ) {
        return flightRepository
                .findAllByDepartureTimeBetweenAndSourceAirportCodeAndDestinationAirportCode(
                        departureDate.atStartOfDay(),
                        departureDate.plusDays(1).atStartOfDay(),
                        sourceAirportCode,
                        destinationAirportCode
                );
    }

    @Transactional(readOnly = true)
    public List<TicketsResponse> handleOneWayRequest(
            String sourceAirportCode,
            String destinationAirportCode,
            LocalDate departureDate,
            SeatType seatType,
            int personCount
    ) {
        List<Flight> flights = getFlightsByAirports(
                sourceAirportCode,
                destinationAirportCode,
                departureDate
        );

        List<TicketsResponse> ticketsResponses = new ArrayList<>();
        for (Flight flight : flights) {
            getSeatByType(flight, seatType, personCount)
                    .ifPresent(seat -> ticketsResponses
                            .add(mapToResponse(flight, seat, personCount)));
        }

        return ticketsResponses;
    }

    @Transactional(readOnly = true)
    public List<Pair<TicketsResponse, TicketsResponse>> handleTwoWayRequest(
            String sourceAirportCode,
            String destinationAirportCode,
            LocalDate departureDate,
            LocalDate returnDepartureDate,
            SeatType seatType,
            int personCount
    ) {
        List<Flight> flights = getFlightsByAirports(
                sourceAirportCode,
                destinationAirportCode,
                departureDate
        );
        List<Flight> returnFlights = getFlightsByAirports(
                destinationAirportCode,
                sourceAirportCode,
                returnDepartureDate
        );

        //return all matching pairs of direct and return flights
        List<Pair<TicketsResponse, TicketsResponse>> twoWayResponses = new ArrayList<>();
        for (Flight flight : flights) {
            Optional<Seat> seat = getSeatByType(flight, seatType, personCount);

            //the problem here is that every return flight
            //is processed inside the second loop several times
            //but I can't think of better solution in this situation for now
            for (Flight returnFlight : returnFlights) {
                Optional<Seat> returnSeat = getSeatByType(returnFlight, seatType, personCount);

                //test if both seats exist
                //and there are at least two hours between the flights
                if (seat.isPresent()
                        && returnSeat.isPresent()
                        && flight.getArrivalTime().plusHours(2).isBefore(returnFlight.getDepartureTime())) {

                    twoWayResponses.add(Pair.of(
                            mapToResponse(flight, seat.get(), personCount),
                            mapToResponse(returnFlight, returnSeat.get(), personCount))
                    );
                }
            }
        }

        return twoWayResponses;
    }

    public Optional<Seat> getSeatByType(Flight flight, SeatType seatType, int personCount) {
        List<Seat> seats = flight
                .getSeats()
                .stream()
                .filter(s -> s.getType().equals(seatType)
                        && s.getBooking() == null)
                .toList();
        if (!seats.isEmpty() && seats.size() >= personCount) {
            return Optional.of(seats.get(0));
        }
        return Optional.empty();
    }

    public LocalTime getEstimatedTime(LocalDateTime departureTime, LocalDateTime arrivalTime) {
        Duration duration = Duration.between(departureTime, arrivalTime);
        return LocalTime.of(
                duration.toHoursPart(),
                duration.toMinutesPart()
        );
    }

    private TicketsResponse mapToResponse(Flight flight, Seat seat, int personCount) {
        return new TicketsResponse(
                flight.getId(),
                seat.getPrice() * personCount,
                flight.getDepartureTime(),
                flight.getArrivalTime(),
                flight.getSourceAirport().getCity(),
                flight.getDestinationAirport().getCity(),
                getEstimatedTime(flight.getDepartureTime(), flight.getArrivalTime())
        );
    }
}
