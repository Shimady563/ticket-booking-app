package com.shimady.ticketbookingapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shimady.ticketbookingapp.controller.dto.TicketResponse;
import com.shimady.ticketbookingapp.model.SeatType;
import com.shimady.ticketbookingapp.service.TicketService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketController.class)
public class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketService ticketService;

    @Test
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

        given(ticketService.handleOneWayRequest(
                eq(sourceAirportCode),
                eq(destinationAirportCode),
                eq(departureDate),
                eq(seatType),
                eq(personCount))
        )
                .willReturn(List.of(ticketResponse));

        // removed time fields from checking because of formatting issues
        mockMvc.perform(get("/tickets/one-way").accept(MediaType.APPLICATION_JSON)
                        .param("sourceAirportCode", sourceAirportCode)
                        .param("destinationAirportCode", destinationAirportCode)
                        .param("departureDate", departureDate.toString())
                        .param("seatType", seatType.toString())
                        .param("personCount", String.valueOf(personCount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].flightId").value(1L))
                .andExpect(jsonPath("$[0].overallPrice").value(1000))
                .andExpect(jsonPath("$[0].departureCity").value("city1"))
                .andExpect(jsonPath("$[0].arrivalCity").value("city2"));
    }
}
