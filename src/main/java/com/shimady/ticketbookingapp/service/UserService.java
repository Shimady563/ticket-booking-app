package com.shimady.ticketbookingapp.service;

import com.shimady.ticketbookingapp.model.User;
import com.shimady.ticketbookingapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//spring security integration will be added in the future updates
@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public User getUserById(Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(RuntimeException::new);
    }

    @Transactional
    public void updateUser(User user) {
        userRepository.save(user);
    }
}
