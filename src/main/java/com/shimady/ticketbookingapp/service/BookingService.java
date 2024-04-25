package com.shimady.ticketbookingapp.service;

import com.shimady.ticketbookingapp.controller.dto.BookingRequest;
import com.shimady.ticketbookingapp.controller.dto.BookingResponse;
import com.shimady.ticketbookingapp.controller.dto.PassengerResponse;
import com.shimady.ticketbookingapp.controller.dto.SeatsResponse;
import com.shimady.ticketbookingapp.model.Booking;
import com.shimady.ticketbookingapp.model.Seat;
import com.shimady.ticketbookingapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

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
    public void handleCreationRequest(BookingRequest request) {
        // for now hardcoding user search
        // in the future user will be retrieved trough userDetails service impl
        Long id = 1L;
        User user = userService.getUserById(id);
        List<Seat> seats = seatsService.getAllSeatsByIds(request.seatsIds());

        Booking booking = new Booking();
        booking.setSeats(new HashSet<>(seats));
        booking.setPassengers(request.passengers());
        user.addBooking(booking);
        userService.updateUser(user);
    }
}
