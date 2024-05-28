package com.shimady.ticketbookingapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shimady.ticketbookingapp.TestSecurityConfig;
import com.shimady.ticketbookingapp.controller.dto.SeatResponse;
import com.shimady.ticketbookingapp.model.SeatType;
import com.shimady.ticketbookingapp.service.SeatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestSecurityConfig.class)
@WebMvcTest(SeatController.class)
public class SeatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SeatService seatService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithAnonymousUser
    public void shouldReturnTicketsInfoByFlightIdAndType() throws Exception {
        SeatType type = SeatType.ECONOMY;
        Long flightId = 1L;

        SeatResponse seatResponse1 = new SeatResponse(
                1L,
                "1",
                1000,
                type,
                true
        );
        SeatResponse seatResponse2 = new SeatResponse(
                2L,
                "2",
                2000,
                type,
                false
        );

        List<SeatResponse> responses = List.of(seatResponse1, seatResponse2);

        given(seatService.getSeatsByFlightIdAndType(
                eq(flightId),
                eq(type)
        ))
                .willReturn(responses);

        mockMvc.perform(get("/seats")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("flightId", flightId.toString())
                        .param("seatType", type.toString()))
                .andExpect(status().isOk())
                .andExpect(content()
                        .json(objectMapper
                                .writeValueAsString(responses)));
    }
}
