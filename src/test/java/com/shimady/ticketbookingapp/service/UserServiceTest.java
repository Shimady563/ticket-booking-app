package com.shimady.ticketbookingapp.service;

import com.shimady.ticketbookingapp.controller.dto.UserRequest;
import com.shimady.ticketbookingapp.exception.ResourceNotFoundException;
import com.shimady.ticketbookingapp.model.User;
import com.shimady.ticketbookingapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

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
        Long userId = 1L;

        given(userRepository.findById(eq(userId))).willReturn(Optional.empty());

        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() ->
                        userService.getUserById(userId));
    }

    @Test
    public void shouldUpdateUser() {
        User user = new User();

        userService.updateUser(user);

        then(userRepository).should().save(eq(user));
    }

    @Test
    public void shouldUpdateUserUsingRequest() {
        User user = new User();
        user.setId(1L);
        user.setUsername("username");
        user.setEmail("email");
        user.setPassword("password");

        UserRequest request = new UserRequest(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword()
        );

        given(userRepository.findById(any())).willReturn(Optional.of(user));

        userService.updateUser(request);

        then(userRepository).should().save(eq(user));
    }

    @Test
    public void shouldThrowExceptionWhenAccessToUpdatingDenied() {
        User retrievedUser = new User();
        retrievedUser.setId(1L);
        UserRequest request = new UserRequest(
                2L,
                "username",
                "email",
                "password"
        );

        given(userRepository.findById(any())).willReturn(Optional.of(retrievedUser));

        assertThatExceptionOfType(AccessDeniedException.class)
                .isThrownBy(() -> userService.updateUser(request));
    }

    @Test
    public void shouldCreateNewUser() {
        UserRequest userRequest = new UserRequest(
                null,
                "username",
                "email",
                "password"
        );

        userService.createUser(userRequest);

        then(userRepository).should().save(any(User.class));
    }
}
