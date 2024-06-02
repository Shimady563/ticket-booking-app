package com.shimady.ticketbookingapp.controller;

import com.shimady.ticketbookingapp.controller.dto.TicketResponse;
import com.shimady.ticketbookingapp.model.SeatType;
import com.shimady.ticketbookingapp.service.TicketService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Validated
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
            @RequestParam @NotBlank String sourceAirportCode,
            @RequestParam @NotBlank String destinationAirportCode,
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
            @RequestParam @NotBlank String sourceAirportCode,
            @RequestParam @NotBlank String destinationAirportCode,
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
