package com.shimady.ticketbookingapp.event;

import com.shimady.ticketbookingapp.model.User;
import com.shimady.ticketbookingapp.service.EmailService;
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
public class CreationEventListener {

    private final TokenService tokenService;
    private final UserService userService;
    private final EmailService emailService;

    @Autowired
    public CreationEventListener(TokenService tokenService, UserService userService, EmailService emailService) {
        this.tokenService = tokenService;
        this.userService = userService;
        this.emailService = emailService;
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(UserCreatedEvent.class)
    public void onUserCreationEvent(UserCreatedEvent event) {
        log.info("Received user creation event: {}", event);
        User user = userService.getUserById(event.userId());
        tokenService.createToken(user);
    }

    @Async
    @TransactionalEventListener(TokenCreatedEvent.class)
    public void onTokenCreationEvent(TokenCreatedEvent event) {
        log.info("Received token creation event: {}", event);
        String mailBody = "Please follow the link to verify your account: http://localhost:8080/token?token=" + event.token();
        emailService.sendEmail(event.email(), "Token verification", mailBody);
    }
}

