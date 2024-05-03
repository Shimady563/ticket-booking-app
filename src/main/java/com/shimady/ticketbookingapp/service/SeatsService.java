package com.shimady.ticketbookingapp.service;

import com.shimady.ticketbookingapp.controller.dto.SeatsResponse;
import com.shimady.ticketbookingapp.model.Seat;
import com.shimady.ticketbookingapp.model.SeatType;
import com.shimady.ticketbookingapp.repository.SeatsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class SeatsService {

    private final SeatsRepository seatsRepository;

    @Autowired
    public SeatsService(SeatsRepository seatsRepository) {
        this.seatsRepository = seatsRepository;
    }

    @Transactional(readOnly = true)
    public List<SeatsResponse> getSeatsByFlightIdAndType(Long flightId, SeatType seatType) {
        log.info("Getting seats of type {} for flight {}", seatType, flightId);
        return seatsRepository
                .findAllByFlightIdAndType(flightId, seatType)
                .stream()
                .map((seat -> new SeatsResponse(
                        seat.getId(),
                        seat.getNumber(),
                        seat.getPrice(),
                        seat.getType(),
                        seat.isBooked()
                )))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Seat> getAllSeatsByIds(List<Long> seatIds) {
        log.info("Getting seats with ids {}", seatIds);
        return seatsRepository.findAllById(seatIds);
    }

    protected SeatsResponse mapToResponse(Seat seat) {
        return new SeatsResponse(
                seat.getId(),
                seat.getNumber(),
                seat.getPrice(),
                seat.getType(),
                seat.isBooked()
        );
    }
}
