package com.shimady.ticketbookingapp.controller.dto;

import com.shimady.ticketbookingapp.model.SeatType;

public record SeatResponse(
        Long id,
        String number,
        int price,
        SeatType type,
        Boolean isBooked
) {
}
