package com.shimady.ticketbookingapp.service;

import com.shimady.ticketbookingapp.controller.dto.TicketResponse;
import com.shimady.ticketbookingapp.model.Flight;
import com.shimady.ticketbookingapp.model.Seat;
import com.shimady.ticketbookingapp.model.SeatType;
import com.shimady.ticketbookingapp.repository.FlightRepository;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class TicketService {

    private final FlightRepository flightRepository;

    @Autowired
    public TicketService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Transactional(readOnly = true)
    public List<Flight> getFlightsByAirports(
            String sourceAirportCode,
            String destinationAirportCode,
            LocalDate departureDate
    ) {
        log.info("Getting flights from airport {}" +
                ", to airport {}, on {}",
                sourceAirportCode, destinationAirportCode, departureDate);
        return flightRepository
                .findAllByDepartureTimeBetweenAndSourceAirportCodeAndDestinationAirportCode(
                        departureDate.atStartOfDay(),
                        departureDate.plusDays(1).atStartOfDay(),
                        sourceAirportCode,
                        destinationAirportCode
                );
    }

    @Transactional(readOnly = true)
    public List<TicketResponse> handleOneWayRequest(
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

        List<TicketResponse> ticketRespons = new ArrayList<>();
        for (Flight flight : flights) {
            getSeatByType(flight, seatType, personCount)
                    .ifPresent(seat -> ticketRespons
                            .add(mapToResponse(flight, seat, personCount)));
        }

        log.info("Processed request for one-way tickets with type {}" +
                " from airport {}" +
                " to airport {}, on {}",
                seatType, sourceAirportCode, destinationAirportCode, departureDate);
        return ticketRespons;
    }

    @Transactional(readOnly = true)
    public List<Pair<TicketResponse, TicketResponse>> handleTwoWayRequest(
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
        List<Pair<TicketResponse, TicketResponse>> twoWayResponses = new ArrayList<>();
        for (Flight flight : flights) {
            Optional<Seat> seatOptional = getSeatByType(flight, seatType, personCount);

            if (seatOptional.isPresent()) {
                Seat seat = seatOptional.get();

                for (Flight returnFlight : returnFlights) {
                    Optional<Seat> returnSeat = getSeatByType(returnFlight, seatType, personCount);

                    //test if there are at least two hours between the flights
                    if (returnSeat.isPresent()
                            && flight.getArrivalTime().plusHours(2).isBefore(returnFlight.getDepartureTime())) {
                        twoWayResponses.add(Pair.of(
                                mapToResponse(flight, seat, personCount),
                                mapToResponse(returnFlight, returnSeat.get(), personCount))
                        );
                    }
                }
            }
        }

        log.info("Processed request for two-way tickets with type {}" +
                        " from airport {}" +
                        " to airport {}, on {}",
                seatType, sourceAirportCode, destinationAirportCode, departureDate);
        return twoWayResponses;
    }

    public Optional<Seat> getSeatByType(Flight flight, SeatType seatType, int personCount) {
        log.info("Getting seat for flight {}" +
                " if it has more than {}" +
                " unbooked seats with type {}", flight.getId(), personCount, seatType);
        List<Seat> seats = flight
                .getSeats()
                .stream()
                .filter(seat -> seat.getType().equals(seatType) && !seat.isBooked())
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

    private TicketResponse mapToResponse(Flight flight, Seat seat, int personCount) {
        return new TicketResponse(
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
