package com.shimady.ticketbookingapp.service;

import com.shimady.ticketbookingapp.event.TokenCreatedEvent;
import com.shimady.ticketbookingapp.exception.ResourceNotFoundException;
import com.shimady.ticketbookingapp.exception.TokenExpiredException;
import com.shimady.ticketbookingapp.model.Token;
import com.shimady.ticketbookingapp.model.User;
import com.shimady.ticketbookingapp.repository.TokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
public class TokenService {

    private final TokenRepository tokenRepository;
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public TokenService(TokenRepository tokenRepository, UserService userService, ApplicationEventPublisher eventPublisher) {
        this.tokenRepository = tokenRepository;
        this.userService = userService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional(readOnly = true)
    public Token getToken(String tokenBody) {
        log.info("Getting token {}", tokenBody);
        return tokenRepository.findByToken(tokenBody)
                .orElseThrow(() -> new ResourceNotFoundException("Token " + tokenBody + " not found"));
    }

    @Transactional
    public void deleteToken(Token token) {
        log.info("Deleting token {}", token);
        tokenRepository.delete(token);
    }

    @Transactional(readOnly = true)
    public void saveToken(Token token) {
        log.info("Saving token {}", token.getToken());
        tokenRepository.save(token);
    }

    @Transactional
    public void createToken(User user) {
        String tokenBody = UUID.randomUUID().toString();
        Token token = new Token(tokenBody, user);
        saveToken(token);
        eventPublisher.publishEvent(new TokenCreatedEvent(token.getToken(), user.getEmail()));
    }

    @Transactional
    public void verifyToken(String tokenBody) {
        log.info("Verifying token {}", tokenBody);
        Token token = getToken(tokenBody);

        if (token.isExpired()) {
            throw new TokenExpiredException("Token " + tokenBody + " is expired");
        }

        userService.enableUser(token.getUser());
        deleteToken(token);
    }

    @Transactional
    public void updateTokenValidity(String tokenBody) {
        log.info("Updating expiration date for token {}", tokenBody);
        Token token = getToken(tokenBody);
        token.updateExpirationDate();
        saveToken(token);
    }
}
