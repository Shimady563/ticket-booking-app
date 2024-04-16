package com.shimady.ticketbookingapp.repository;

import com.shimady.ticketbookingapp.model.Flight;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FlightRepository extends Repository<Flight, Long> {

    Optional<Flight> findById(Long flightId);

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = {"seats", "seats.booking", "sourceAirport", "destinationAirport"})
    List<Flight> findAllByDepartureTimeBetweenAndSourceAirportCodeAndDestinationAirportCodeAllIgnoreCase(
            LocalDateTime startOfDay,
            LocalDateTime endOfDay,
            String sourceAirportCode,
            String destinationAirportCode);
}
