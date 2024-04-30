package com.shimady.ticketbookingapp.controller;

import com.shimady.ticketbookingapp.controller.dto.SeatsResponse;
import com.shimady.ticketbookingapp.model.SeatType;
import com.shimady.ticketbookingapp.service.SeatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/seats")
public class SeatsController {

    private final SeatsService seatsService;

    @Autowired
    public SeatsController(SeatsService seatsService) {
        this.seatsService = seatsService;
    }

    @GetMapping("")
    public List<SeatsResponse> getSeats(@RequestParam Long flightId, @RequestParam SeatType seatType) {
        return seatsService
                .getSeatsByFlightIdAndType(flightId, seatType)
                .stream()
                .map((s) -> new SeatsResponse(
                        s.getId(),
                        s.getNumber(),
                        s.getPrice(),
                        s.getType(),
                        s.getBooking() != null)
                )
                .toList();
    }
}
