package com.shimady.ticketbookingapp.controller;

import com.shimady.ticketbookingapp.controller.dto.TicketResponse;
import com.shimady.ticketbookingapp.model.SeatType;
import com.shimady.ticketbookingapp.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Ticket Controller", description = "Controller for viewing information about flights and available one-way and two-way tickets")
public class TicketController {

    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/one-way")
    @Operation(description = "Getting one-way tickets information")
    public List<TicketResponse> getOneWayTicketsInfo(
            @RequestParam
            @NotBlank
            @Parameter(description = "Flight source airport code", example = "VKO")
            String sourceAirportCode,

            @RequestParam
            @NotBlank
            @Parameter(description = "Flight destination airport name", example = "SVO")
            String destinationAirportCode,

            @RequestParam
            @Parameter(description = "Flight departure date", example = "2024-08-24")
            LocalDate departureDate,

            @RequestParam(required = false, defaultValue = "ECONOMY")
            @Parameter(description = "Flight destination airport name", example = "ECONOMY")
            SeatType seatType,

            @RequestParam(required = false, defaultValue = "1")
            @Parameter(description = "Number of passengers who will take the flight")
            int personCount
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
    @Operation(description = "Getting two-way tickets information")
    public List<Pair<TicketResponse, TicketResponse>> getTwoWayTicketsInfo(
            @RequestParam
            @NotBlank
            @Parameter(description = "Flight source airport code", example = "VKO")
            String sourceAirportCode,

            @RequestParam
            @NotBlank
            @Parameter(description = "Flight destination airport name", example = "SVO")
            String destinationAirportCode,

            @RequestParam
            @Parameter(description = "Flight departure date", example = "2024-08-24")
            LocalDate departureDate,

            @RequestParam
            @Parameter(description = "Return flight departure date", example = "2024-08-26")
            LocalDate returnDepartureDate,

            @RequestParam(required = false, defaultValue = "ECONOMY")
            @Parameter(description = "Flight destination airport name", example = "ECONOMY")
            SeatType seatType,

            @RequestParam(required = false, defaultValue = "1")
            @Parameter(description = "Number of passengers who will take the flight")
            int personCount
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
