package com.shimady.ticketbookingapp.service;

import com.shimady.ticketbookingapp.controller.dto.TicketResponse;
import com.shimady.ticketbookingapp.model.*;
import com.shimady.ticketbookingapp.repository.FlightRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.util.Pair;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private FlightRepository flightRepository;

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

        List<Flight> responses = ticketService.getFlightsByAirports(
                sourceAirportCode,
                destinationAirportCode,
                departureDate
        );

        assertThat(responses).isEqualTo(flights);
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


        given(flightRepository
                .findAllByDepartureTimeBetweenAndSourceAirportCodeAndDestinationAirportCode(
                        eq(departureDate.atStartOfDay()),
                        eq(departureDate.plusDays(1).atStartOfDay()),
                        eq(sourceAirportCode),
                        eq(destinationAirportCode)
                ))
                .willReturn(flights);

        List<TicketResponse> responses = ticketService.handleOneWayRequest(
                sourceAirportCode,
                destinationAirportCode,
                departureDate,
                seatType,
                personCount
        );

        assertThat(responses)
                .hasSize(1)
                .extracting(
                        TicketResponse::overallPrice,
                        TicketResponse::departureTime,
                        TicketResponse::arrivalTime)
                .containsExactly(
                        tuple(
                                seatPrice * personCount,
                                departureTime,
                                arrivalTime
                        )
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

        given(flightRepository
                .findAllByDepartureTimeBetweenAndSourceAirportCodeAndDestinationAirportCode(
                        eq(departureDate.atStartOfDay()),
                        eq(departureDate.plusDays(1).atStartOfDay()),
                        eq(sourceAirportCode),
                        eq(destinationAirportCode)
                ))
                .willReturn(flights);
        given(flightRepository
                .findAllByDepartureTimeBetweenAndSourceAirportCodeAndDestinationAirportCode(
                        eq(returnDepartureDate.atStartOfDay()),
                        eq(returnDepartureDate.plusDays(1).atStartOfDay()),
                        eq(destinationAirportCode),
                        eq(sourceAirportCode)
                ))
                .willReturn(returnFlights);

        List<Pair<TicketResponse, TicketResponse>> responses = ticketService.handleTwoWayRequest(
                sourceAirportCode,
                destinationAirportCode,
                departureDate,
                returnDepartureDate,
                seatType,
                personCount
        );

        assertThat(responses).hasSize(1);
        assertThat(List.of(responses.get(0).getFirst(), responses.get(0).getSecond()))
                .extracting(
                        TicketResponse::overallPrice,
                        TicketResponse::departureTime,
                        TicketResponse::arrivalTime
                )
                .containsExactly(
                        tuple(
                                seatPrice * personCount,
                                departureTime1,
                                arrivalTime1
                        ),
                        tuple(
                                seatPrice * personCount,
                                departureTime2,
                                arrivalTime2
                        )
                );
    }
}
