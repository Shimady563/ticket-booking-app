package com.shimady.ticketbookingapp.controller;

import com.shimady.ticketbookingapp.service.TokenService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/token")
public class TokenController {

    private final TokenService tokenService;

    @Autowired
    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void verifyToken(@RequestParam("token") @NotBlank String tokenBody) {
        tokenService.verifyToken(tokenBody);
    }

    @GetMapping("/update")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void revokeToken(@RequestParam("token") @NotBlank String tokenBody) {
        tokenService.updateTokenValidity(tokenBody);
    }
}
