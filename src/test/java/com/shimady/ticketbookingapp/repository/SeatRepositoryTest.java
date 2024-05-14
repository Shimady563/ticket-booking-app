package com.shimady.ticketbookingapp.repository;

import com.shimady.ticketbookingapp.model.Flight;
import com.shimady.ticketbookingapp.model.Seat;
import com.shimady.ticketbookingapp.model.SeatType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@DataJpaTest
public class SeatRepositoryTest {

    public final SeatType seatType = SeatType.ECONOMY;
    public final SeatType otherSeatType = SeatType.BUSINESS;
    private final List<Long> seatIds = new ArrayList<>();
    private Long flightId;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    public void setUp() {
        Seat seat1 = new Seat();
        seat1.setType(seatType);
        Seat seat2 = new Seat();
        seat2.setType(otherSeatType);
        Seat seat3 = new Seat();
        Flight flight = new Flight();
        flight.addSeat(seat1);
        flight.addSeat(seat2);

        entityManager.persist(flight);
        entityManager.persist(seat1);
        entityManager.persist(seat2);
        entityManager.persist(seat3);

        entityManager.flush();

        flightId = flight.getId();
        seatIds.add(seat1.getId());
        seatIds.add(seat2.getId());
        seatIds.add(seat3.getId());
    }

    @Test
    public void testFindAllById() {
        List<Seat> seats = seatRepository.findAllById(seatIds);
        assertThat(seats)
                .hasSameSizeAs(seatIds)
                .extracting(Seat::getId)
                .containsAll(seatIds);
    }

    @Test
    public void testFindAllByFlightIdAndType() {
        List<Seat> seats = seatRepository.findAllByFlightIdAndType(flightId, seatType);

        assertThat(seats)
                .hasSize(1)
                .extracting(
                        seat -> seat.getFlight().getId(),
                        Seat::getType
                )
                .containsExactly(
                        tuple(
                                flightId,
                                seatType
                        )
                );
    }
}
