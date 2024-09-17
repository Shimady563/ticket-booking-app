package com.shimady.ticketbookingapp.controller;

import com.shimady.ticketbookingapp.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/token")
@Tag(name = "Token Controller", description = "Controller for manipulating Auth token")
public class TokenController {

    private final TokenService tokenService;

    @Autowired
    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(description = "Verifying auth token. The link with token is sent to email after registration")
    public void verifyToken(
            @RequestParam("token")
            @NotBlank
            @Parameter(description = "Auth token value", example = "bbde62ac-639e-4589-a378-08078711a463")
            String tokenBody
    ) {
        tokenService.verifyToken(tokenBody);
    }

    @GetMapping("/update")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(description = "Prolong token validity if it's expired")
    public void revokeToken(
            @RequestParam("token")
            @NotBlank
            @Parameter(description = "Auth token value", example = "bbde62ac-639e-4589-a378-08078711a463")
            String tokenBody
    ) {
        tokenService.updateTokenValidity(tokenBody);
    }
}
