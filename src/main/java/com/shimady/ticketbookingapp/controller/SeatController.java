package com.shimady.ticketbookingapp.controller;

import com.shimady.ticketbookingapp.controller.dto.SeatResponse;
import com.shimady.ticketbookingapp.model.SeatType;
import com.shimady.ticketbookingapp.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/seats")
public class SeatController {

    private final SeatService seatService;

    @Autowired
    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @GetMapping("")
    public List<SeatResponse> getSeats(@RequestParam Long flightId, @RequestParam SeatType seatType) {
        return seatService.getSeatsByFlightIdAndType(flightId, seatType);
    }
}
