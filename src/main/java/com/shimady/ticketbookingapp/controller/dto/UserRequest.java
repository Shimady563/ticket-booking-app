package com.shimady.ticketbookingapp.controller.dto;

import com.shimady.ticketbookingapp.validation.Password;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request entity for providing user information")
public record UserRequest(
        @Schema(example = "1")
        Long id,

        @NotBlank(message = "username cannot be blank")
        @Size(min = 4, max = 64,
                message = "username length should be between 4 and 64 characters")
        @Schema(example = "Username")
        String username,

        @Email(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$",
                message = "wrong format of email")
        @Schema(example = "email@mail.com")
        String email,

        @Password(message = "password should be at least 8 characters long " +
                "and contain at least one lower case letter, " +
                "upper case letter, " +
                "digit, " +
                "symbol from @#$%^&+=")
        @Schema(example = "StrongPassword123$")
        String password
) {
}
