package com.shimady.ticketbookingapp.controller;

import com.shimady.ticketbookingapp.controller.dto.BookingRequest;
import com.shimady.ticketbookingapp.controller.dto.BookingResponse;
import com.shimady.ticketbookingapp.model.Booking;
import com.shimady.ticketbookingapp.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/book")
    public ResponseEntity<String> bookSeats(@RequestBody BookingRequest request) {
        //exceptions and handling for them will be added in the future
        if (request.passengers().size() != request.seatsIds().size()) {
            throw new RuntimeException();
        }
        bookingService.handleCreationRequest(request);
        return new ResponseEntity<>("Booking successfully created", HttpStatus.CREATED);
    }

    @GetMapping("")
    public List<BookingResponse> getBookings() {
        return bookingService.getBookingsByUser();
    }
}
