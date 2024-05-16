package com.shimady.ticketbookingapp.service;

import com.shimady.ticketbookingapp.controller.dto.BookingRequest;
import com.shimady.ticketbookingapp.controller.dto.BookingResponse;
import com.shimady.ticketbookingapp.controller.dto.PassengersResponse;
import com.shimady.ticketbookingapp.controller.dto.SeatResponse;
import com.shimady.ticketbookingapp.exception.BadRequestException;
import com.shimady.ticketbookingapp.exception.BookingException;
import com.shimady.ticketbookingapp.model.Booking;
import com.shimady.ticketbookingapp.model.Passenger;
import com.shimady.ticketbookingapp.model.Seat;
import com.shimady.ticketbookingapp.model.User;
import com.shimady.ticketbookingapp.repository.BookingRepository;
import com.shimady.ticketbookingapp.repository.PassengerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserService userService;

    @Mock
    private SeatService seatService;

    @Mock
    private PassengerRepository passengerRepository;

    @InjectMocks
    private BookingService bookingService;

    @Test
    public void shouldHandleCreationRequest() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setBookings(new HashSet<>());

        List<Long> seatIds = List.of(1L, 2L);
        Seat seat1 = new Seat();
        seat1.setId(seatIds.get(0));
        Seat seat2 = new Seat();
        seat2.setId(seatIds.get(1));
        List<Seat> seats = List.of(seat1, seat2);

        Passenger passenger1 = new Passenger();
        passenger1.setFirstName("name1");
        Passenger passenger2 = new Passenger();
        passenger2.setFirstName("name2");
        Set<Passenger> passengers = Set.of(passenger1, passenger2);

        given(userService.getUserById(eq(userId))).willReturn(user);
        given(seatService.getAllSeatsByIds(eq(seatIds))).willReturn(seats);

        bookingService.handleCreationRequest(
                new BookingRequest(
                        seatIds,
                        passengers
                )
        );

        then(userService).should().updateUser(eq(user));
        then(passengerRepository).should().saveAll(eq(passengers));
        then(bookingRepository).should().save(any());
    }

    @Test
    public void shouldThrowAnExceptionWhenSeatsNotFound() {
        List<Long> seatIds = List.of(1L, 2L);
        Seat seat1 = new Seat();
        seat1.setId(seatIds.get(0));
        List<Seat> seats = List.of(seat1);

        given(seatService.getAllSeatsByIds(eq(seatIds))).willReturn(seats);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> bookingService.handleCreationRequest(
                        new BookingRequest(
                                seatIds,
                                null
                        )
                ));
    }

    @Test
    public void shouldThrowAnExceptionWhenSeatIsBooked() {
        List<Long> seatIds = List.of(1L);
        Seat seat1 = new Seat();
        seat1.setId(seatIds.get(0));
        seat1.setBooking(new Booking());
        List<Seat> seats = List.of(seat1);

        given(seatService.getAllSeatsByIds(eq(seatIds))).willReturn(seats);

        assertThatExceptionOfType(BookingException.class)
                .isThrownBy(() -> bookingService
                        .handleCreationRequest(
                                new BookingRequest(
                                        seatIds,
                                        null
                                )
                        ));
    }

    @Test
    public void shouldReturnBookingsForUser() {
        Long id = 1L;
        String firstName = "name";
        Passenger passenger = new Passenger();
        passenger.setFirstName(firstName);
        Seat seat = new Seat();
        seat.setId(id);
        Booking booking = new Booking();
        seat.setBooking(booking);
        booking.setPassengers(Set.of(passenger));
        booking.setSeats(Set.of(seat));

        User user = new User();
        user.setId(id);
        user.setBookings(Set.of(booking));

        given(userService.getUserById(eq(id))).willReturn(user);
        given(seatService
                .mapToResponse(eq(seat)))
                .willReturn(
                        new SeatResponse(
                                seat.getId(),
                                seat.getNumber(),
                                seat.getPrice(),
                                seat.getType(),
                                seat.isBooked()

                        )
                );

        List<BookingResponse> responses = bookingService.getBookingsByUser();

        assertThat(responses).hasSize(1);

        BookingResponse bookingResponse = responses.get(0);

        assertThat(bookingResponse.seats())
                .hasSize(1)
                .extracting(SeatResponse::id)
                .contains(id);
        assertThat(bookingResponse.passengers())
                .hasSize(1)
                .extracting(PassengersResponse::firstName)
                .contains(firstName);
    }
}
