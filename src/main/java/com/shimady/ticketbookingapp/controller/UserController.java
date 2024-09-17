package com.shimady.ticketbookingapp.controller;

import com.shimady.ticketbookingapp.controller.dto.UserInfo;
import com.shimady.ticketbookingapp.controller.dto.UserRequest;
import com.shimady.ticketbookingapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Tag(name = "User Controller", description = "Controller for creating, authenticating and updating users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Creating new user account")
    public void signup(@Valid @RequestBody UserRequest request) {
        userService.createUser(request);
    }

    @GetMapping("")
    @Operation(summary = "Obtaining current user information")
    @SecurityRequirement(name = "SessionAuth")
    public UserInfo getUser() {
        return userService.getCurrentUserInfo();
    }

    @PutMapping("")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Updating current user account")
    @SecurityRequirement(name = "SessionAuth")
    public void updateUser(@Valid @RequestBody UserRequest request) {
        userService.updateUser(request);
    }

    // for documenting logout endpoint
    @PostMapping("/logout")
    @Operation(summary = "Logout current user")
    @SecurityRequirement(name = "SessionAuth")
    public void logout() {
    }

    // for documenting login endpoint
    // I use request params to , but I couldn't find a way to represent it in Swagger
    @PostMapping(value = "/login")
    @Operation(
            summary = "Login user",
            description = "Spring uses form-data for login by default, " +
                    "but I couldn't find a way to represent it in Swagger, " +
                    "you can find the right request format in the postman documentation")
    public void login(
            @RequestParam("username")
            @Parameter(description = "User username. ", example = "Username")
            String email,

            @RequestParam("password")
            @Parameter(description = "User password", example = "StrongPassword123$")
            String password
    ) {
    }
}
