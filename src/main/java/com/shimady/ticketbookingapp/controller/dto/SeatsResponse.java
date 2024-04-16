package com.shimady.ticketbookingapp.controller.dto;

import com.shimady.ticketbookingapp.model.Booking;
import com.shimady.ticketbookingapp.model.SeatType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SeatsResponse {
    private Long id;
    private String number;
    private int price;
    private SeatType type;
    private Booking booking;
}
