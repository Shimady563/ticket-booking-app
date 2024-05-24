package com.shimady.ticketbookingapp.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserCreatedEvent {
    private final Long userId;
}
