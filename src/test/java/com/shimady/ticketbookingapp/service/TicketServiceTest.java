package com.shimady.ticketbookingapp.service;

import com.shimady.ticketbookingapp.model.Airport;
import com.shimady.ticketbookingapp.model.Flight;
import com.shimady.ticketbookingapp.model.Seat;
import com.shimady.ticketbookingapp.model.SeatType;
import com.shimady.ticketbookingapp.repository.FlightRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @Spy
    @InjectMocks
    private TicketService ticketService;

    @Test
    public void testGetFlightsByAirports() {
        String sourceAirportCode = "QWE";
        String destinationAirportCode = "RTY";
        LocalDate departureDate = LocalDate.now();
        Airport sourceAirport = new Airport("airport1", "city1", sourceAirportCode);
        Airport destinationAirport = new Airport("airport2", "city2", destinationAirportCode);

        Flight flight1 = new Flight();
        flight1.setSourceAirport(sourceAirport);
        flight1.setDestinationAirport(destinationAirport);

        List<Flight> flights = List.of(flight1);

        given(flightRepository
                .findAllByDepartureTimeBetweenAndSourceAirportCodeAndDestinationAirportCode(
                        eq(departureDate.atStartOfDay()),
                        eq(departureDate.plusDays(1).atStartOfDay()),
                        eq(sourceAirportCode),
                        eq(destinationAirportCode)
                ))
                .willReturn(flights);

        ticketService.getFlightsByAirports(
                sourceAirportCode,
                destinationAirportCode,
                departureDate
        );

        then(flightRepository).should()
                .findAllByDepartureTimeBetweenAndSourceAirportCodeAndDestinationAirportCode(
                        eq(departureDate.atStartOfDay()),
                        eq(departureDate.plusDays(1).atStartOfDay()),
                        eq(sourceAirportCode),
                        eq(destinationAirportCode)
                );
    }

    @Test
    public void testHandleOneWayRequest() {
        String sourceAirportCode = "QWE";
        String destinationAirportCode = "RTY";
        LocalDate departureDate = LocalDate.now();
        SeatType seatType = SeatType.ECONOMY;
        int personCount = 2;
        int seatPrice = 100;

        LocalDateTime departureTime = LocalDateTime.now().plusMinutes(1);
        LocalDateTime arrivalTime = departureTime.plusHours(6);

        Airport sourceAirport = new Airport("Airport", "City", sourceAirportCode);
        Airport destinationAirport = new Airport("Airport2", "City2", destinationAirportCode);

        Seat seat1 = new Seat();
        seat1.setPrice(seatPrice);
        seat1.setType(seatType);
        Seat seat2 = new Seat();
        seat2.setType(seatType);
        seat2.setPrice(seatPrice);

        List<Flight> flights = List.of(
                new Flight(departureTime,
                        arrivalTime,
                        sourceAirport,
                        destinationAirport,
                        Set.of(seat1, seat2))
        );

        willReturn(flights).given(ticketService).getFlightsByAirports(
                eq(sourceAirportCode),
                eq(destinationAirportCode),
                eq(departureDate)
        );

        ticketService.handleOneWayRequest(
                sourceAirportCode,
                destinationAirportCode,
                departureDate,
                seatType,
                personCount
        );

        then(ticketService).should()
                .getFlightsByAirports(
                        eq(sourceAirportCode),
                        eq(destinationAirportCode),
                        eq(departureDate)
                );
    }

    @Test
    public void testHandleTwoWayRequest() {
        String sourceAirportCode = "QWE";
        String destinationAirportCode = "RTY";
        LocalDate departureDate = LocalDate.now();
        LocalDate returnDepartureDate = LocalDate.now();
        SeatType seatType = SeatType.ECONOMY;
        int personCount = 2;
        int seatPrice = 100;

        LocalDateTime departureTime1 = LocalDateTime.now().plusMinutes(1);
        LocalDateTime arrivalTime1 = departureTime1.plusHours(6);
        LocalDateTime departureTime2 = arrivalTime1.plusHours(3);
        LocalDateTime arrivalTime2 = departureTime2.plusHours(6);

        Airport sourceAirport = new Airport("Airport", "City", sourceAirportCode);
        Airport destinationAirport = new Airport("Airport2", "City2", destinationAirportCode);

        Seat seat1 = new Seat();
        seat1.setPrice(seatPrice);
        seat1.setType(seatType);
        Seat seat2 = new Seat();
        seat2.setType(seatType);
        seat2.setPrice(seatPrice);
        Seat seat3 = new Seat();
        seat3.setPrice(seatPrice);
        seat3.setType(seatType);
        Seat seat4 = new Seat();
        seat4.setType(seatType);
        seat4.setPrice(seatPrice);

        List<Flight> flights = List.of(
                new Flight(departureTime1,
                        arrivalTime1,
                        sourceAirport,
                        destinationAirport,
                        Set.of(seat1, seat2))
        );

        List<Flight> returnFlights = List.of(
                new Flight(departureTime2,
                        arrivalTime2,
                        destinationAirport,
                        sourceAirport,
                        Set.of(seat3, seat4))
        );

        willReturn(flights).given(ticketService).getFlightsByAirports(
                eq(sourceAirportCode),
                eq(destinationAirportCode),
                eq(departureDate)
        );
        willReturn(returnFlights).given(ticketService).getFlightsByAirports(
                eq(destinationAirportCode),
                eq(sourceAirportCode),
                eq(returnDepartureDate)
        );

        ticketService.handleTwoWayRequest(
                sourceAirportCode,
                destinationAirportCode,
                departureDate,
                returnDepartureDate,
                seatType,
                personCount
        );

        then(ticketService).should().getFlightsByAirports(
                eq(sourceAirportCode),
                eq(destinationAirportCode),
                eq(departureDate)
        );
        then(ticketService).should().getFlightsByAirports(
                eq(destinationAirportCode),
                eq(sourceAirportCode),
                eq(returnDepartureDate)
        );
    }
}
