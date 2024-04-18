package com.shimady.ticketbookingapp.service;

import com.shimady.ticketbookingapp.model.Seat;
import com.shimady.ticketbookingapp.model.SeatType;
import com.shimady.ticketbookingapp.repository.FlightRepository;
import com.shimady.ticketbookingapp.repository.SeatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SeatsService {

    private final SeatsRepository seatsRepository;

    @Autowired
    public SeatsService(SeatsRepository seatsRepository, FlightRepository flightRepository) {
        this.seatsRepository = seatsRepository;
    }

    @Transactional(readOnly = true)
    public List<Seat> getSeatsByFlightIdAndType(Long flightId, SeatType seatType) {
        //proper exceptions and handling for them will be added soon
        return seatsRepository.findAllByFlightIdAndType(flightId, seatType);
    }
}
