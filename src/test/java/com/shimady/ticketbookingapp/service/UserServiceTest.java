package com.shimady.ticketbookingapp.service;

import com.shimady.ticketbookingapp.exception.ResourceNotFoundException;
import com.shimady.ticketbookingapp.model.User;
import com.shimady.ticketbookingapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private UserService userService;
    
    @Test
    public void shouldGetUserById() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        given(userRepository.findById(eq(userId))).willReturn(Optional.of(user));

        User receivedUser = userService.getUserById(userId);

        assertThat(receivedUser.getId()).isEqualTo(userId);

    }
    
    @Test
    public void shouldThrowAnExceptionWhenUserNotFound() {
        given(userRepository.findById(eq(1L))).willReturn(Optional.empty());

        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() ->
                        userService.getUserById(1L));
    }
}
