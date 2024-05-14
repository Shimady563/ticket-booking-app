package com.shimady.ticketbookingapp.service;

import com.shimady.ticketbookingapp.controller.dto.SeatResponse;
import com.shimady.ticketbookingapp.model.Flight;
import com.shimady.ticketbookingapp.model.Seat;
import com.shimady.ticketbookingapp.model.SeatType;
import com.shimady.ticketbookingapp.repository.SeatRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class SeatServiceTest {

    @Mock
    private SeatRepository seatRepository;

    @InjectMocks
    private SeatService seatService;

    @Test
    public void testGetSeatsByFlightIdAndType() {
        Long flightId = 1L;
        Flight flight1 = new Flight();
        flight1.setId(flightId);

        SeatType seatType = SeatType.ECONOMY;
        Seat seat1 = new Seat();
        seat1.setType(seatType);
        seat1.setFlight(flight1);

        List<Seat> seats = List.of(seat1);

        given(seatRepository.
                findAllByFlightIdAndType(
                        eq(flightId),
                        eq(seatType)
                ))
                .willReturn(seats);

        List<SeatResponse> receivedSeats = seatService.getSeatsByFlightIdAndType(
                flightId,
                seatType
        );

        assertThat(receivedSeats)
                .hasSize(1)
                .extracting(SeatResponse::type)
                .isEqualTo(List.of(seatType));
    }

    @Test
    public void testGetAllSeatsByIds() {
        List<Long> ids = List.of(1L, 2L);
        Seat seat1 = new Seat();
        seat1.setId(ids.get(0));
        Seat seat2 = new Seat();
        seat2.setId(ids.get(1));

        List<Seat> seats = List.of(seat1, seat2);

        given(seatRepository.findAllById(eq(ids))).willReturn(seats);

        List<Seat> receivedSeats = seatService.getAllSeatsByIds(ids);

        assertThat(receivedSeats)
                .hasSize(2)
                .extracting(Seat::getId)
                .containsExactly(1L, 2L);
    }
}
