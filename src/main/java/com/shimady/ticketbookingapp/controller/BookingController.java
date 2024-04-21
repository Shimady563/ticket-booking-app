package com.shimady.ticketbookingapp.controller;

import com.shimady.ticketbookingapp.controller.dto.BookingRequest;
import com.shimady.ticketbookingapp.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/book")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("")
    public ResponseEntity<String> bookSeats(@RequestBody BookingRequest request) {
        //exceptions and handling for them will be added in the future
        if (request.passengers().size() != request.seatsIds().size()) {
            throw new RuntimeException();
        }
        bookingService.handleRequest(request);
        return new ResponseEntity<>("Booking successfully created", HttpStatus.CREATED);
    }
}
