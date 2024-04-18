package com.shimady.ticketbookingapp.repository;

import com.shimady.ticketbookingapp.model.Seat;
import com.shimady.ticketbookingapp.model.SeatType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface SeatsRepository extends Repository<Seat, Long> {

    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, attributePaths = {"booking"})
    List<Seat> findAllByFlightIdAndType(Long flightId, SeatType seatType);
}
