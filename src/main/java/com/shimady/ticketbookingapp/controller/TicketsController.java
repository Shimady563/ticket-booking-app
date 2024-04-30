package com.shimady.ticketbookingapp.controller;

import com.shimady.ticketbookingapp.controller.dto.TicketsResponse;
import com.shimady.ticketbookingapp.model.SeatType;
import com.shimady.ticketbookingapp.service.TicketsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketsController {

    private final TicketsService ticketsService;

    @Autowired
    public TicketsController(TicketsService ticketsService) {
        this.ticketsService = ticketsService;
    }

    @GetMapping("/one-way")
    public List<TicketsResponse> getOneWayTicketsInfo(
            @RequestParam String sourceAirportCode,
            @RequestParam String destinationAirportCode,
            @RequestParam LocalDate departureDate,
            @RequestParam(required = false, defaultValue = "ECONOMY") SeatType seatType,
            @RequestParam(required = false, defaultValue = "1") int personCount
    ) {
        return ticketsService.handleOneWayRequest(
                sourceAirportCode,
                destinationAirportCode,
                departureDate,
                seatType,
                personCount
        );
    }

    @GetMapping("/two-way")
    public List<Pair<TicketsResponse, TicketsResponse>> getTwoWayTicketsInfo(
            @RequestParam String sourceAirportCode,
            @RequestParam String destinationAirportCode,
            @RequestParam LocalDate departureDate,
            @RequestParam LocalDate returnDepartureDate,
            @RequestParam(required = false, defaultValue = "ECONOMY") SeatType seatType,
            @RequestParam(required = false, defaultValue = "1") int personCount
    ) {
        return ticketsService.handleTwoWayRequest(
                sourceAirportCode,
                destinationAirportCode,
                departureDate,
                returnDepartureDate,
                seatType,
                personCount
        );
    }
}
