package com.shimady.ticketbookingapp.controller;

import com.shimady.ticketbookingapp.controller.dto.SeatResponse;
import com.shimady.ticketbookingapp.model.SeatType;
import com.shimady.ticketbookingapp.service.SeatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SeatController.class)
public class SeatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SeatService seatService;

    @Test
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

        given(seatService.getSeatsByFlightIdAndType(
                eq(flightId),
                eq(type)
        ))
                .willReturn(List.of(seatResponse1, seatResponse2));

        mockMvc.perform(get("/seats").accept(MediaType.APPLICATION_JSON)
                        .param("flightId", flightId.toString())
                        .param("seatType", type.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].number").value("1"))
                .andExpect(jsonPath("$[0].price").value("1000"))
                .andExpect(jsonPath("$[0].type").value(type.toString()))
                .andExpect(jsonPath("$[0].isBooked").value("true"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].number").value("2"))
                .andExpect(jsonPath("$[1].price").value("2000"))
                .andExpect(jsonPath("$[1].type").value(type.toString()))
                .andExpect(jsonPath("$[1].isBooked").value("false"));
    }
}
