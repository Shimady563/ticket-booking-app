package com.shimady.ticketbookingapp.controller;

import com.shimady.ticketbookingapp.TestSecurityConfig;
import com.shimady.ticketbookingapp.service.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestSecurityConfig.class)
@WebMvcTest(TokenController.class)
public class TokenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TokenService tokenService;

    @Test
    @WithAnonymousUser
    public void shouldVerifyToken() throws Exception {
        String tokenBody = "token";

        mockMvc.perform(get("/token")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("token", tokenBody))
                .andExpect(status().isNoContent());

        then(tokenService).should().verifyToken(eq(tokenBody));
    }

    @Test
    @WithAnonymousUser
    public void shouldRevokeToken() throws Exception {
        String tokenBody = "token";

        mockMvc.perform(get("/token/update")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("token", tokenBody))
                .andExpect(status().isNoContent());

        then(tokenService).should().updateTokenValidity(eq(tokenBody));
    }
}
