package com.shimady.ticketbookingapp.controller;

import com.shimady.ticketbookingapp.controller.dto.SeatResponse;
import com.shimady.ticketbookingapp.model.SeatType;
import com.shimady.ticketbookingapp.service.SeatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/seats")
@Tag(name = "Seat Controller", description = "Controller for viewing seat information")
public class SeatController {

    private final SeatService seatService;

    @Autowired
    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @GetMapping("")
    @Operation(summary = "Getting flight seats information")
    public List<SeatResponse> getSeats(
            @RequestParam
            @Parameter(description = "Flight id", example = "1")
            Long flightId,

            @RequestParam
            @Parameter(description = "Seat type", example = "ECONOMY")
            SeatType seatType
    ) {
        return seatService.getSeatsByFlightIdAndType(flightId, seatType);
    }
}
