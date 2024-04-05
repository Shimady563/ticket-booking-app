package com.shimady.ticketbookingapp.repository;

import com.shimady.ticketbookingapp.model.Flight;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.Repository;

import java.time.LocalDateTime;
import java.util.List;

public interface FlightRepository extends Repository<Flight, Long> {

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = {"seats", "seats.booking", "sourceAirport", "destinationAirport"})
    List<Flight> findAllByDepartureTimeBetweenAndSourceAirportCodeIgnoreCaseAndDestinationAirportCodeIgnoreCase(
            LocalDateTime startOfDay,
            LocalDateTime endOfDay,
            String sourceAirportCode,
            String destinationAirportCode);
}
