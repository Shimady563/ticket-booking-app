package com.shimady.ticketbookingapp.service;

import com.shimady.ticketbookingapp.controller.dto.BookingRequest;
import com.shimady.ticketbookingapp.controller.dto.BookingResponse;
import com.shimady.ticketbookingapp.controller.dto.PassengersResponse;
import com.shimady.ticketbookingapp.controller.dto.SeatsResponse;
import com.shimady.ticketbookingapp.exception.BadRequestException;
import com.shimady.ticketbookingapp.exception.BookingException;
import com.shimady.ticketbookingapp.model.Booking;
import com.shimady.ticketbookingapp.model.Passenger;
import com.shimady.ticketbookingapp.model.Seat;
import com.shimady.ticketbookingapp.model.User;
import com.shimady.ticketbookingapp.repository.BookingRepository;
import com.shimady.ticketbookingapp.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final SeatsService seatsService;
    private final PassengerRepository passengerRepository;

    @Autowired
    public BookingService(
            SeatsService seatsService,
            UserService userService,
            PassengerRepository passengerRepository,
            BookingRepository bookingRepository
    ) {
        this.seatsService = seatsService;
        this.userService = userService;
        this.passengerRepository = passengerRepository;
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public void handleCreationRequest(BookingRequest request) {
        // for now hardcoding user search
        // in the future user will be retrieved trough userDetails service impl
        Long id = 1L;
        User user = userService.getUserById(id);
        List<Seat> seats = seatsService.getAllSeatsByIds(request.seatsIds());

        if (seats.size() != request.seatsIds().size()) {
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
                .map((passenger -> passengerRepository.findByFirstNameAndLastNameAndPassportNumber(
                        passenger.getFirstName(),
                        passenger.getLastName(),
                        passenger.getPassportNumber()
                ).orElse(passenger))).collect(Collectors.toSet());

        //saving and updating entities by hand
        //because cascade was always updating seats before booking
        //so the exception about non-existing booking was thrown every time
        Booking booking = new Booking();
        booking.setSeats(new HashSet<>(seats));
        booking.setPassengers(existingPassengers);
        user.addBooking(booking);
        userService.updateUser(user);
        bookingRepository.save(booking);
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getBookingsByUser() {
        Long id = 1L;
        User user = userService.getUserById(id);
        return user
                .getBookings()
                .stream()
                .map((booking -> new BookingResponse(
                        booking.getCreationTime(),
                        booking.getSeats().stream().map((seat -> new SeatsResponse(
                                seat.getId(),
                                seat.getNumber(),
                                seat.getPrice(),
                                seat.getType(),
                                seat.getBooking()
                        ))).collect(Collectors.toSet()),
                        booking.getPassengers().stream().map(passenger -> new PassengersResponse(
                                passenger.getFirstName(),
                                passenger.getLastName(),
                                passenger.getBirthDate(),
                                passenger.getCitizenship(),
                                passenger.getPassportNumber(),
                                passenger.getPassportExpiryDate()
                        )).collect(Collectors.toSet())
                )))
                .toList();
    }
}
