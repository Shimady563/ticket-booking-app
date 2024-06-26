package com.shimady.ticketbookingapp.service;

import com.shimady.ticketbookingapp.controller.dto.SeatResponse;
import com.shimady.ticketbookingapp.model.Seat;
import com.shimady.ticketbookingapp.model.SeatType;
import com.shimady.ticketbookingapp.repository.SeatRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class SeatService {

    private final SeatRepository seatRepository;

    @Autowired
    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    @Transactional(readOnly = true)
    public List<SeatResponse> getSeatsByFlightIdAndType(Long flightId, SeatType seatType) {
        log.info("Getting seats of type {} for flight {}", seatType, flightId);
        return seatRepository
                .findAllByFlightIdAndType(flightId, seatType)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Seat> getAllSeatsByIds(List<Long> seatIds) {
        log.info("Getting seats with ids {}", seatIds);
        return seatRepository.findAllById(seatIds);
    }

    protected SeatResponse mapToResponse(Seat seat) {
        return new SeatResponse(
                seat.getId(),
                seat.getNumber(),
                seat.getPrice(),
                seat.getType(),
                seat.isBooked()
        );
    }
}
