package com.shimady.ticketbookingapp.event;

import com.shimady.ticketbookingapp.model.User;
import com.shimady.ticketbookingapp.service.TokenService;
import com.shimady.ticketbookingapp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
public class EventListener {

    private final TokenService tokenService;
    private final UserService userService;

    @Autowired
    public EventListener(TokenService tokenService, UserService userService) {
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(UserCreatedEvent.class)
    public void onAuthorizationEvent(UserCreatedEvent event) {
        log.info("Received user creation event: {}", event);
        User user = userService.getUserById(event.getUserId());
        tokenService.createToken(user);
    }
}

