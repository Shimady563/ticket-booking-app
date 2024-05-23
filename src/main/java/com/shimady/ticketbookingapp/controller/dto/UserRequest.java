package com.shimady.ticketbookingapp.controller.dto;

public record UserRequest(
        Long id,
        String username,
        String email,
        String password
) {
}
