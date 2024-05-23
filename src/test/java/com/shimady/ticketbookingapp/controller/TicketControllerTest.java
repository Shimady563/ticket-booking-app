package com.shimady.ticketbookingapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shimady.ticketbookingapp.TestSecurityConfig;
import com.shimady.ticketbookingapp.controller.dto.TicketResponse;
import com.shimady.ticketbookingapp.model.SeatType;
import com.shimady.ticketbookingapp.service.TicketService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestSecurityConfig.class)
@WebMvcTest(TicketController.class)
public class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketService ticketService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithAnonymousUser
    public void shouldReturnOneWayTicketsInfo() throws Exception {
        LocalDateTime departureTime = LocalDateTime.now();
        LocalDateTime arrivalTime = LocalDateTime.now().plusHours(6);
        LocalTime estimatedTime = LocalTime.of(6, 0);
        LocalDate departureDate = LocalDate.now();

        TicketResponse ticketResponse = new TicketResponse(
                1L,
                1000,
                departureTime,
                arrivalTime,
                "city1",
                "city2",
                estimatedTime
        );

        String sourceAirportCode = "QWE";
        String destinationAirportCode = " RTY";
        SeatType seatType = SeatType.ECONOMY;
        int personCount = 1;

        List<TicketResponse> responses = List.of(ticketResponse);

        given(ticketService.handleOneWayRequest(
                eq(sourceAirportCode),
                eq(destinationAirportCode),
                eq(departureDate),
                eq(seatType),
                eq(personCount))
        )
                .willReturn(responses);

        mockMvc.perform(get("/tickets/one-way")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("sourceAirportCode", sourceAirportCode)
                        .param("destinationAirportCode", destinationAirportCode)
                        .param("departureDate", departureDate.toString())
                        .param("seatType", seatType.toString())
                        .param("personCount", String.valueOf(personCount)))
                .andExpect(status().isOk())
                .andExpect(content()
                        .json(objectMapper.writeValueAsString(responses))
                );
    }

    @Test
    @WithAnonymousUser
    public void shouldReturnTwoWayTicketsInfo() throws Exception {
        LocalDateTime departureTime1 = LocalDate.now().atStartOfDay();
        LocalDateTime arrivalTime1 = departureTime1.plusHours(6);
        LocalDateTime departureTime2 = departureTime1.plusHours(9);
        LocalDateTime arrivalTime2 = departureTime2.plusHours(6);

        LocalTime estimatedTime1 = LocalTime.of(6, 0);
        LocalTime estimatedTime2 = LocalTime.of(6, 0);
        LocalDate departureDate = LocalDate.now();

        TicketResponse ticketResponse1 = new TicketResponse(
                1L,
                1000,
                departureTime1,
                arrivalTime1,
                "city1",
                "city2",
                estimatedTime1
        );

        TicketResponse ticketResponse2 = new TicketResponse(
                2L,
                2000,
                departureTime2,
                arrivalTime2,
                "city2",
                "city1",
                estimatedTime2

        );

        String sourceAirportCode = "QWE";
        String destinationAirportCode = " RTY";
        SeatType seatType = SeatType.ECONOMY;
        int personCount = 1;

        List<Pair<TicketResponse, TicketResponse>> responses = List.of(
                Pair.of(ticketResponse1, ticketResponse2)
        );

        given(ticketService.handleTwoWayRequest(
                eq(sourceAirportCode),
                eq(destinationAirportCode),
                eq(departureDate),
                eq(departureDate),
                eq(seatType),
                eq(personCount))
        )
                .willReturn(responses);

        mockMvc.perform(get("/tickets/two-way")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("sourceAirportCode", sourceAirportCode)
                        .param("destinationAirportCode", destinationAirportCode)
                        .param("departureDate", departureDate.toString())
                        .param("returnDepartureDate", departureDate.toString())
                        .param("seatType", seatType.toString())
                        .param("personCount", String.valueOf(personCount)))
                .andExpect(status().isOk())
                .andExpect(content()
                        .json(objectMapper
                                .writeValueAsString(responses)));
    }
}
