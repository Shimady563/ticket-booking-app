package com.shimady.ticketbookingapp.service;

import com.shimady.ticketbookingapp.event.TokenCreatedEvent;
import com.shimady.ticketbookingapp.exception.ResourceNotFoundException;
import com.shimady.ticketbookingapp.exception.TokenExpiredException;
import com.shimady.ticketbookingapp.model.Token;
import com.shimady.ticketbookingapp.model.User;
import com.shimady.ticketbookingapp.repository.TokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private UserService userService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Spy
    @InjectMocks
    private TokenService tokenService;

    @Test
    public void shouldGetTokenByTokenBody() {
        String tokenBody = "token";
        Token token = new Token(tokenBody, new User());

        given(tokenRepository.findByToken(eq(tokenBody))).willReturn(Optional.of(token));

        tokenService.getToken(tokenBody);

        then(tokenRepository).should().findByToken(eq(tokenBody));
    }

    @Test
    public void shouldThrowExceptionWhenTokenNotFound() {
        String tokenBody = "token";

        given(tokenRepository.findByToken(eq(tokenBody))).willReturn(Optional.empty());

        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> tokenService.getToken("token"));
    }

    @Test
    public void shouldDeleteToken() {
        Token token = new Token("token", new User());

        tokenService.deleteToken(token);

        then(tokenRepository).should().delete(eq(token));
    }

    @Test
    public void shouldSaveToken() {
        Token token = new Token("token", new User());

        tokenService.saveToken(token);

        then(tokenRepository).should().save(eq(token));
    }

    @Test
    public void shouldCreateToken() {
        User user = new User();

        willDoNothing().given(tokenService).saveToken(any(Token.class));

        tokenService.createToken(user);

        then(eventPublisher).should().publishEvent(any(TokenCreatedEvent.class));
    }

    @Test
    public void shouldVerifyToken() {
        User user = new User();
        String tokenBody = "token";
        Token token = new Token(tokenBody, user);

        willReturn(token).given(tokenService).getToken(eq(tokenBody));

        tokenService.verifyToken(tokenBody);

        then(userService).should().enableUser(eq(user));
    }

    @Test
    public void shouldThrowExceptionWhenTokenIsExpired() throws NoSuchFieldException, IllegalAccessException {
        User user = new User();
        String tokenBody = "token";
        Token token = new Token(tokenBody, user);

        Field expirationDateField = token.getClass().getDeclaredField("expirationDate");
        expirationDateField.setAccessible(true);
        expirationDateField.set(token, LocalDateTime.now().minusDays(1));

        willReturn(token).given(tokenService).getToken(eq(tokenBody));

        assertThatExceptionOfType(TokenExpiredException.class)
                .isThrownBy(() -> tokenService.verifyToken(tokenBody));
    }

    @Test
    public void shouldUpdateTokenValidity() {
        String tokenBody = "token";
        Token token = new Token(tokenBody, new User());

        willReturn(token).given(tokenService).getToken(eq(tokenBody));
        willDoNothing().given(tokenService).saveToken(eq(token));

        tokenService.updateTokenValidity(tokenBody);

        then(tokenService).should().getToken(eq(tokenBody));
        then(tokenService).should().saveToken(eq(token));
    }
}
