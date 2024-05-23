package com.shimady.ticketbookingapp.service;

import com.shimady.ticketbookingapp.controller.dto.UserInfo;
import com.shimady.ticketbookingapp.controller.dto.UserRequest;
import com.shimady.ticketbookingapp.exception.ResourceNotFoundException;
import com.shimady.ticketbookingapp.model.User;
import com.shimady.ticketbookingapp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public User getUserById(Long userId) {
        log.info("Getting user with id {}", userId);
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));
    }

    @Transactional
    protected void updateUser(User user) {
        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getAuthorities()
        );
        SecurityContextHolder
                .getContext()
                .setAuthentication(newAuth);

        userRepository.save(user);
        log.info("Updated user {} with username {}", user.getId(), user.getUsername());
    }

    @Transactional
    public void updateUser(UserRequest request) {
        User updatedUser = mapToUser(request);
        User user = retrieveCurrentUser();

        if (!user.getId().equals(updatedUser.getId())) {
            throw new AccessDeniedException("User id in request does not match current user id");
        }

        user.setUsername(updatedUser.getUsername());
        user.setEmail(updatedUser.getEmail());
        user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));

        updateUser(user);
    }

    @Transactional
    public void createUser(UserRequest request) {
        User user = mapToUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        log.info("Created user {} with username {}", user.getId(), user.getUsername());
    }

    @Transactional(readOnly = true)
    protected User retrieveCurrentUser() {
        Long userId = getCurrentUserInfo().id();
        return getUserById(userId);
    }

    public UserInfo getCurrentUserInfo() {
        log.info("Getting current user from security context");
        return mapToInfo(
                (User) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal()
        );
    }

    private UserInfo mapToInfo(User user) {
        return new UserInfo(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }

    private User mapToUser(UserRequest request) {
        User user = new User();
        user.setId(request.id());
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(request.password());
        return user;
    }
}
