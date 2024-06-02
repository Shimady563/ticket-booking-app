package com.shimady.ticketbookingapp.controller.dto;

import com.shimady.ticketbookingapp.validation.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(
        Long id,

        @NotBlank(message = "username cannot be blank")
        @Size(min = 4, max = 64,
                message = "username length should be between 4 and 64 characters")
        String username,

        @Email(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$",
                message = "wrong format of email")
        String email,

        @Password(message = "password should be at least 8 characters long " +
                "and contain at least one lower case letter, " +
                "upper case letter, " +
                "digit, " +
                "symbol from @#$%^&+=")
        String password
) {
}
