package com.shimady.ticketbookingapp.repository;

import com.shimady.ticketbookingapp.model.Airport;
import com.shimady.ticketbookingapp.model.Booking;
import com.shimady.ticketbookingapp.model.Flight;
import com.shimady.ticketbookingapp.model.Seat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@DataJpaTest
public class FlightRepositoryTest {

    public final LocalDate departureDate = LocalDate.now();
    public final String sourceAirportCode = "QWE";
    public final String destinationAirportCode = "RTY";

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    public void setUp() {
        LocalDateTime departureTime = LocalDateTime.now().plusMinutes(1);
        LocalDateTime arrivalTime = LocalDateTime.now().plusHours(6);
        Airport sourceAirport = new Airport("Airport", "City", sourceAirportCode);
        Airport destinationAirport = new Airport("Airport2", "City2", destinationAirportCode);
        Booking booking = new Booking();
        Booking booking2 = new Booking();
        Seat seat = new Seat();
        Seat seat2 = new Seat();
        booking.setSeats(Set.of(seat));
        booking2.setSeats(Set.of(seat2));

        Flight flight = new Flight(departureTime, arrivalTime, sourceAirport, destinationAirport, Set.of(seat));
        Flight flight2 = new Flight(departureTime, arrivalTime, destinationAirport, sourceAirport, Set.of(seat2));

        entityManager.persist(seat);
        entityManager.persist(seat2);
        entityManager.persist(booking);
        entityManager.persist(booking2);
        entityManager.persist(destinationAirport);
        entityManager.persist(sourceAirport);
        entityManager.persist(flight);
        entityManager.persist(flight2);
        entityManager.flush();
    }

    @Test
    public void testFindAllByDepartureTimeBetweenAndSourceAirportCodeAndDestinationAirportCode() {
        List<Flight> flights = flightRepository.findAllByDepartureTimeBetweenAndSourceAirportCodeAndDestinationAirportCode(
                departureDate.atStartOfDay(),
                departureDate.plusDays(1).atStartOfDay(),
                sourceAirportCode,
                destinationAirportCode
        );

        assertThat(flights)
                .hasSize(1)
                .extracting(
                        flight -> flight.getSourceAirport().getCode(),
                        flight -> flight.getDestinationAirport().getCode()
                )
                .contains(
                        tuple(
                                sourceAirportCode,
                                destinationAirportCode
                        )
                );
        assertThat(flights.get(0).getSeats())
                .hasSize(1)
                .extracting(Seat::isBooked)
                .isEqualTo(true);

    }
}
