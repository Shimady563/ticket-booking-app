package com.shimady.ticketbookingapp.service;

import com.shimady.ticketbookingapp.controller.dto.BookingRequest;
import com.shimady.ticketbookingapp.model.Booking;
import com.shimady.ticketbookingapp.model.Seat;
import com.shimady.ticketbookingapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookingService {

    private final UserService userService;
    private final SeatsService seatsService;

    @Autowired
    public BookingService(SeatsService seatsService, UserService userService) {
        this.seatsService = seatsService;
        this.userService = userService;
    }

    @Transactional
    public void handleRequest(BookingRequest request) {
        // for now hardcoding user search
        // in the future user will be retrieved trough userDetails service impl
        Long id = 1L;
        User user = userService.getUserById(id);
        List<Seat> seats = seatsService.getAllSeatsByIds(request.seatsIds());

        Booking booking = new Booking();
        booking.setSeats(seats);
        booking.addAllPassengers(request.passengers());
        user.addBooking(booking);
        userService.updateUser(user);
    }
}
