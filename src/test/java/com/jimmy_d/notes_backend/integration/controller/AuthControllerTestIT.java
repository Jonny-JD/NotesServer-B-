package com.jimmy_d.notes_backend.integration.controller;

import com.jimmy_d.notes_backend.dto.LoginRequest;
import com.jimmy_d.notes_backend.dto.UserCreateDto;
import com.jimmy_d.notes_backend.service.UserService;
import com.jimmy_d.notes_backend.test_utils.WithMockCustomUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthControllerTestIT extends ControllerTestBase {

    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService.createUser(new UserCreateDto(
                "john",
                "password123",
                "jonny@mail.com",
                Set.of("USER")
        ));
    }

    // ─── login ────────────────────────────────────────────────

    @Test
    void login_success() throws Exception {
        var request = new LoginRequest("john", "password123");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.username").value("john"))
                .andExpect(jsonPath("$.email").value("jonny@mail.com"));
    }

    @Test
    void login_wrongPassword_returnsUnauthorized() throws Exception {
        var request = new LoginRequest("john", "wrongpassword");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_userNotFound_returnsUnauthorized() throws Exception {
        var request = new LoginRequest("ghost_user", "password123");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_blankUsername_returnsBadRequest() throws Exception {
        var request = new LoginRequest("", "password123");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    // ─── me ───────────────────────────────────────────────────

    @Test
    @WithMockCustomUser(username = "john", email = "jonny@mail.com")
    void me_authenticated_returnsUserInfo() throws Exception {
        mockMvc.perform(get("/api/v1/auth/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john"))
                .andExpect(jsonPath("$.email").value("jonny@mail.com"));
    }

    @Test
    void me_unauthenticated_returnsForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/auth/me"))
                .andExpect(status().isForbidden());
    }
}