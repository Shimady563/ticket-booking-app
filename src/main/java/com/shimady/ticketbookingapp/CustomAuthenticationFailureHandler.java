package com.shimady.ticketbookingapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.shimady.ticketbookingapp.exception.ApplicationError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    //probably a bad solution, but I couldn't come up with something better
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        log.error(exception.getMessage());

        PrintWriter writer = response.getWriter();

        ApplicationError error = new ApplicationError(
                exception.getMessage(),
                HttpStatus.UNAUTHORIZED.value()
        );

        ObjectMapper objectMapper = new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .registerModule(new JavaTimeModule());

        writer.write(objectMapper.writeValueAsString(error));
        writer.flush();
        writer.close();
    }
}