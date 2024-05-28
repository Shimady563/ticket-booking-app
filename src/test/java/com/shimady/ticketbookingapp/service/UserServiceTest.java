package com.shimady.ticketbookingapp.service;

import com.shimady.ticketbookingapp.controller.dto.UserInfo;
import com.shimady.ticketbookingapp.controller.dto.UserRequest;
import com.shimady.ticketbookingapp.event.UserCreatedEvent;
import com.shimady.ticketbookingapp.exception.ResourceNotFoundException;
import com.shimady.ticketbookingapp.model.Booking;
import com.shimady.ticketbookingapp.model.User;
import com.shimady.ticketbookingapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ApplicationEventPublisher publisher;

    @Spy
    @InjectMocks
    private UserService userService;

    @Test
    public void shouldGetUserById() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        given(userRepository.findById(eq(userId))).willReturn(Optional.of(user));

        userService.getUserById(userId);

        then(userRepository).should().findById(eq(userId));
    }

    @Test
    public void shouldThrowAnExceptionWhenUserNotFound() {
        given(userRepository.findById(any())).willReturn(Optional.empty());

        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() ->
                        userService.getUserById(1L));
    }

    @Test
    public void shouldUpdateUser() {
        User user = new User();

        userService.updateUser(user);

        then(userRepository).should().save(eq(user));
    }

    @Test
    public void shouldUpdateUserUsingRequest() {
        String password = "password";
        User user = new User();
        user.setId(1L);
        user.setUsername("username");
        user.setEmail("email");
        user.setPassword(password);

        UserRequest request = new UserRequest(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword()
        );

        willReturn(user).given(userService).retrieveCurrentUser();

        userService.updateUser(request);

        then(passwordEncoder).should().encode(eq(password));
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

        willReturn(retrievedUser).given(userService).retrieveCurrentUser();

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
        then(publisher).should().publishEvent(any(UserCreatedEvent.class));
    }


    @Test
    public void shouldRetrieveCurrentUser() {
        Long userId = 1L;
        UserInfo userInfo = new UserInfo(userId, "username", "email");
        User user = new User();
        user.setId(userId);

        willReturn(userInfo).given(userService).getCurrentUserInfo();
        willReturn(user).given(userService).getUserById(eq(userId));

        userService.retrieveCurrentUser();

        then(userService).should().getCurrentUserInfo();
        then(userService).should().getUserById(eq(userId));
    }

    @Test
    public void shouldEnableUser() {
        User user = new User();

        willDoNothing().given(userService).updateUser(eq(user));

        userService.enableUser(user);

        then(userService).should().updateUser(eq(user));
    }

    @Test
    public void shouldReturnAllUsers() {
        given(userRepository.findAllFetchBookings()).willReturn(List.of(new User()));

        userService.getAllUsers();

        then(userRepository).should().findAllFetchBookings();
    }
}
