package com.shimady.ticketbookingapp.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "User entity")
public record UserInfo(
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
        String email
) {
}
