package com.shimady.ticketbookingapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shimady.ticketbookingapp.TestSecurityConfig;
import com.shimady.ticketbookingapp.controller.dto.UserInfo;
import com.shimady.ticketbookingapp.controller.dto.UserRequest;
import com.shimady.ticketbookingapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(TestSecurityConfig.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser
    public void shouldSignup() throws Exception {
        UserRequest request = new UserRequest(
                1L,
                "username",
                "email",
                "password"
        );

        mockMvc.perform(post("/user/signup")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        then(userService).should().createUser(eq(request));
    }

    @Test
    @WithMockUser(username = "username")
    public void shouldReturnCurrentUser() throws Exception {
        UserInfo userInfo = new UserInfo(
                1L,
                "username",
                "email"
        );

        given(userService.getCurrentUserInfo()).willReturn(userInfo);

        mockMvc.perform(get("/user")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userInfo)));
    }

    @Test
    @WithAnonymousUser
    public void getUserShouldReturnUnauthorizedResponseWhenUserNotAuthorized() throws Exception {
        mockMvc.perform(get("/user"))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser
    public void shouldUpdateUser() throws Exception {
        UserRequest request = new UserRequest(
                1L,
                "username",
                "email",
                "password"
        );

        mockMvc.perform(put("/user")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        then(userService).should().updateUser(eq(request));
    }

    @Test
    @WithAnonymousUser
    public void updateUserShouldReturnUnauthorizedResponseWhenUserNotAuthorized() throws Exception {
        mockMvc.perform(put("/user"))
                .andExpect(status().isFound());
    }
}
