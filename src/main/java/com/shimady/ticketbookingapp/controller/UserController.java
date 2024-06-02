package com.shimady.ticketbookingapp.controller;

import com.shimady.ticketbookingapp.controller.dto.UserInfo;
import com.shimady.ticketbookingapp.controller.dto.UserRequest;
import com.shimady.ticketbookingapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(@Valid @RequestBody UserRequest request) {
        userService.createUser(request);
    }

    @GetMapping("")
    public UserInfo getUser() {
        return userService.getCurrentUserInfo();
    }

    @PutMapping("")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUser(@Valid @RequestBody UserRequest request) {
        userService.updateUser(request);
    }
}
