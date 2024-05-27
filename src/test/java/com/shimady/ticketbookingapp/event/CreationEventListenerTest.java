package com.shimady.ticketbookingapp.event;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;

@ExtendWith(SpringExtension.class)
public class CreationEventListenerTest {

    @Autowired
    private ApplicationEventPublisher publisher;

    @MockBean
    private CreationEventListener eventListener;

    @Test
    public void testOnUserCreationEvent() {
        Long userId = 1L;
        UserCreatedEvent event = new UserCreatedEvent(userId);
        publisher.publishEvent(event);

        then(eventListener).should().onUserCreationEvent(eq(event));
    }

    @Test
    public void testOnTokenCreationEvent() {
        String token = "token";
        String email = "email";
        TokenCreatedEvent event = new TokenCreatedEvent(token, email);
        publisher.publishEvent(event);

        then(eventListener).should().onTokenCreationEvent(eq(event));
    }
}
