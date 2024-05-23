package com.shimady.ticketbookingapp.service;

import com.shimady.ticketbookingapp.controller.dto.BookingRequest;
import com.shimady.ticketbookingapp.controller.dto.BookingResponse;
import com.shimady.ticketbookingapp.controller.dto.PassengerResponse;
import com.shimady.ticketbookingapp.exception.BadRequestException;
import com.shimady.ticketbookingapp.exception.BookingException;
import com.shimady.ticketbookingapp.model.Booking;
import com.shimady.ticketbookingapp.model.Passenger;
import com.shimady.ticketbookingapp.model.Seat;
import com.shimady.ticketbookingapp.model.User;
import com.shimady.ticketbookingapp.repository.BookingRepository;
import com.shimady.ticketbookingapp.repository.PassengerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final SeatService seatService;
    private final PassengerRepository passengerRepository;

    @Autowired
    public BookingService(
            SeatService seatService,
            UserService userService,
            PassengerRepository passengerRepository,
            BookingRepository bookingRepository
    ) {
        this.seatService = seatService;
        this.userService = userService;
        this.passengerRepository = passengerRepository;
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public void handleCreationRequest(BookingRequest request) {
        User user = userService.retrieveCurrentUser();
        List<Seat> seats = seatService.getAllSeatsByIds(request.seatIds());

        if (seats.size() != request.seatIds().size()) {
            throw new BadRequestException("Non-existing seat ids in request");
        }

        for (Seat seat : seats) {
            if (seat.isBooked()) {
                throw new BookingException("Seat " + seat.getId() + " is already booked");
            }
        }

        //updating passengers if they already exist to not violate unique constraint
        Set<Passenger> existingPassengers = request.passengers()
                .stream()
                .map((passenger -> passengerRepository
                        .findByFirstNameAndLastNameAndPassportNumber(
                                passenger.getFirstName(),
                                passenger.getLastName(),
                                passenger.getPassportNumber()
                        ).orElse(passenger)))
                .collect(Collectors.toSet());

        //saving and updating entities by hand
        //because cascade was always updating seats before booking
        //so the exception about non-existing booking was thrown every time
        Booking booking = new Booking();
        booking.setSeats(new HashSet<>(seats));
        booking.setPassengers(existingPassengers);
        user.addBooking(booking);
        userService.updateUser(user);
        passengerRepository.saveAll(existingPassengers);
        bookingRepository.save(booking);
        log.info("Created booking {} for user {} with login {}", booking.getId(), user.getId(), user.getUsername());
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getBookingsByUser() {
        User user = userService.retrieveCurrentUser();
        log.info("Getting bookings for user {} with login {}", user.getId(), user.getUsername());
        return user
                .getBookings()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private BookingResponse mapToResponse(Booking booking) {
        return new BookingResponse(
                booking.getCreationTime(),
                booking.getSeats()
                        .stream()
                        .map(seatService::mapToResponse)
                        .collect(Collectors.toSet()),
                booking.getPassengers()
                        .stream()
                        .map(this::mapPassengersToResponse)
                        .collect(Collectors.toSet())
        );
    }

    private PassengerResponse mapPassengersToResponse(Passenger passenger) {
        return new PassengerResponse(
                passenger.getFirstName(),
                passenger.getLastName(),
                passenger.getBirthDate(),
                passenger.getCitizenship(),
                passenger.getPassportNumber(),
                passenger.getPassportExpiryDate()
        );
    }
}
