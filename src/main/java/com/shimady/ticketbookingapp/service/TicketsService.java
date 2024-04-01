package com.shimady.ticketbookingapp.service;

import com.shimady.ticketbookingapp.model.Flight;
import com.shimady.ticketbookingapp.model.Seat;
import com.shimady.ticketbookingapp.model.SeatType;
import com.shimady.ticketbookingapp.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class TicketsService {

    private final FlightRepository flightRepository;

    @Autowired
    public TicketsService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public List<Flight> getFlights(
            String sourceAirportCode,
            String destinationAirportCode,
            LocalDate departureDate
    ) {
        return flightRepository
                .findAllByDepartureTimeBetweenAndSourceAirportCodeIgnoreCaseAndDestinationAirportCodeIgnoreCase(
                        departureDate.atStartOfDay(),
                        departureDate.plusDays(1).atStartOfDay(),
                        sourceAirportCode,
                        destinationAirportCode
                );
    }

    public Optional<Seat> getSeatByType(Flight flight, SeatType seatType) {
        return flight
                .getSeats()
                .stream()
                .filter(
                        s -> s.getType()
                                .equals(seatType)
                )
                .findFirst();
    }

    public LocalTime getEstimatedTime(LocalDateTime departureTime, LocalDateTime arrivalTime) {
        Duration duration = Duration.between(departureTime, arrivalTime);
        return LocalTime.of(
                duration.toHoursPart(),
                duration.toMinutesPart()
        );
    }
}
