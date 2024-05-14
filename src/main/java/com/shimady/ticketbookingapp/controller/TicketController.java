package com.shimady.ticketbookingapp.controller;

import com.shimady.ticketbookingapp.controller.dto.TicketResponse;
import com.shimady.ticketbookingapp.model.SeatType;
import com.shimady.ticketbookingapp.service.TicketService;
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
public class TicketController {

    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/one-way")
    public List<TicketResponse> getOneWayTicketsInfo(
            @RequestParam String sourceAirportCode,
            @RequestParam String destinationAirportCode,
            @RequestParam LocalDate departureDate,
            @RequestParam(required = false, defaultValue = "ECONOMY") SeatType seatType,
            @RequestParam(required = false, defaultValue = "1") int personCount
    ) {
        return ticketService.handleOneWayRequest(
                sourceAirportCode,
                destinationAirportCode,
                departureDate,
                seatType,
                personCount
        );
    }

    @GetMapping("/two-way")
    public List<Pair<TicketResponse, TicketResponse>> getTwoWayTicketsInfo(
            @RequestParam String sourceAirportCode,
            @RequestParam String destinationAirportCode,
            @RequestParam LocalDate departureDate,
            @RequestParam LocalDate returnDepartureDate,
            @RequestParam(required = false, defaultValue = "ECONOMY") SeatType seatType,
            @RequestParam(required = false, defaultValue = "1") int personCount
    ) {
        return ticketService.handleTwoWayRequest(
                sourceAirportCode,
                destinationAirportCode,
                departureDate,
                returnDepartureDate,
                seatType,
                personCount
        );
    }
}
