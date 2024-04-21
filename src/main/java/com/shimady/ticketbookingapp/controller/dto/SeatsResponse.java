package com.shimady.ticketbookingapp.controller.dto;

import com.shimady.ticketbookingapp.model.Booking;
import com.shimady.ticketbookingapp.model.SeatType;

public record SeatsResponse(Long id, String number, int price, SeatType type, Booking booking) {
}
