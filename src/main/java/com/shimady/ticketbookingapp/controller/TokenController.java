package com.shimady.ticketbookingapp.controller;

import com.shimady.ticketbookingapp.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public void verifyToken(@RequestParam("token") String tokenBody) {
        tokenService.verifyToken(tokenBody);
    }

    @GetMapping("/update")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void revokeToken(@RequestParam("token") String tokenBody) {
        tokenService.updateTokenValidity(tokenBody);
    }
}
