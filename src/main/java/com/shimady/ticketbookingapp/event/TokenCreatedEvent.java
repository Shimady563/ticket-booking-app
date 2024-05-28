package com.shimady.ticketbookingapp.event;

public record TokenCreatedEvent(
        String token,
        String email
) {
}
