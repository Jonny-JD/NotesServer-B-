package com.jimmy_d.notes_backend.integration.controller;

import com.jimmy_d.notes_backend.database.entity.Role;
import com.jimmy_d.notes_backend.integration.ControllerTestBase;
import com.jimmy_d.notes_backend.integration.RestTestUtils;
import com.jimmy_d.notes_backend.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
class AuthControllerIT extends ControllerTestBase {

    private final MockMvc mockMvc;
    private final RestTestUtils restTestUtils;


    @Test
    void shouldLoginSuccessfully() throws Exception {
        restTestUtils.createRestUser("test", "test@test.com");

        var body = """
                    {
                        "username": "test",
                        "password": "pass"
                    }
                """;

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.username").value("test"));
        mockMvc.perform(post("/api/v1/auth/logout"));
    }

    @Test
    void shouldFailLoginWithUserNotFound() throws Exception {
        var body = """
                    {
                        "username": "Dummy_user_1",
                        "password": "wrong_password"
                    }
                """;

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.validation").value("User not found by username: [Dummy_user_1]"));
        mockMvc.perform(post("/api/v1/auth/logout"));
    }

    @Test
    void shouldFailLoginWithBadCredentials() throws Exception {
        restTestUtils.createRestUser("exist", "exist@test.com");

        var body = """
                    {
                        "username": "exist",
                        "password": "wrong_password"
                    }
                """;

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid credentials"));
    }

    @Test
    void shouldReturnUnauthorizedOnMeWithoutAuth() throws Exception {
        mockMvc.perform(get("/api/v1/auth/logout"));
        mockMvc.perform(get("/api/v1/auth/me"))
                .andExpect(status().isForbidden())
                .andExpect(result ->
                        assertEquals("Access Denied", result.getResponse().getErrorMessage()));
    }

    @Test
    void shouldReturnUserInfoWhenAuthenticated() throws Exception {
        var userDetails = new CustomUserDetails(1L, "Dummy_user_1", "dummy_1_pass", Set.of(Role.USER));
        var auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);


        mockMvc.perform(get("/api/v1/auth/me")
                        .with(authentication(auth)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("Dummy_user_1"));
    }
}
