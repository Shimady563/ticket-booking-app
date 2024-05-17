package com.shimady.ticketbookingapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shimady.ticketbookingapp.controller.dto.BookingRequest;
import com.shimady.ticketbookingapp.controller.dto.BookingResponse;
import com.shimady.ticketbookingapp.controller.dto.PassengersResponse;
import com.shimady.ticketbookingapp.controller.dto.SeatResponse;
import com.shimady.ticketbookingapp.exception.BadRequestException;
import com.shimady.ticketbookingapp.model.Passenger;
import com.shimady.ticketbookingapp.model.SeatType;
import com.shimady.ticketbookingapp.service.BookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldHandleCreationRequest() throws Exception {
        Passenger passenger1 = new Passenger();
        passenger1.setFirstName("name1");
        Passenger passenger2 = new Passenger();
        passenger2.setFirstName("name2");
        BookingRequest bookingRequest = new BookingRequest(
                List.of(1L, 2L),
                Set.of(passenger1, passenger2)
        );

        mockMvc.perform(post("/bookings/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(bookingRequest))
                )
                .andExpect(status().isCreated());

        then(bookingService).should().handleCreationRequest(eq(bookingRequest));
    }

    @Test
    public void shouldThrowExceptionWhenWrongNumberOfSeatsInRequest() throws Exception {
        Passenger passenger1 = new Passenger();
        passenger1.setFirstName("name1");
        Passenger passenger2 = new Passenger();
        passenger2.setFirstName("name2");
        BookingRequest bookingRequest = new BookingRequest(
                List.of(1L),
                Set.of(passenger1, passenger2)
        );

        mockMvc.perform(post("/bookings/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(bookingRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(
                                result.getResolvedException() instanceof BadRequestException
                        )
                                .isTrue()
                );
    }


    @Test
    public void shouldReturnBookingsForUser() throws Exception {
        Long seatId = 1L;
        String firstName = "name";
        SeatResponse seat = new SeatResponse(
                seatId,
                "1",
                1000,
                SeatType.ECONOMY,
                true
        );
        PassengersResponse passenger = new PassengersResponse(
                firstName,
                "lname",
                LocalDate.now(),
                "russia",
                123L,
                LocalDate.now()

        );
        final LocalDateTime creationDate = LocalDateTime.now();
        BookingResponse bookingResponse = new BookingResponse(
                creationDate,
                Set.of(seat),
                Set.of(passenger)
        );

        List<BookingResponse> responses = List.of(bookingResponse);
        given(bookingService.getBookingsByUser()).willReturn(responses);

        mockMvc.perform(get("/bookings")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .json(objectMapper.
                                writeValueAsString(responses)));
    }
}