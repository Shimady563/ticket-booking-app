package com.shimady.ticketbookingapp.controller;

import com.shimady.ticketbookingapp.controller.dto.BookingRequest;
import com.shimady.ticketbookingapp.controller.dto.BookingResponse;
import com.shimady.ticketbookingapp.exception.BadRequestException;
import com.shimady.ticketbookingapp.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@Tag(name = "Booking Controller", description = "Controller booking tickets and viewing your bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/book")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Book seats for specified people")
    @SecurityRequirement(name = "SessionAuth")
    public void bookSeats(@Valid @RequestBody BookingRequest request) {
        if (request.passengers().size() != request.seatIds().size()) {
            throw new BadRequestException("Wrong number of passengers or seats");
        }

        bookingService.handleCreationRequest(request);
    }

    @GetMapping("")
    @Operation(summary = "Get current user bookings")
    @SecurityRequirement(name = "SessionAuth")
    public List<BookingResponse> getBookings() {
        return bookingService.getBookingsByUser();
    }
}
