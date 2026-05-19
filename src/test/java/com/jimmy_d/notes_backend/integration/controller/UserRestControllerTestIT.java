package com.jimmy_d.notes_backend.integration.controller;

import com.jimmy_d.notes_backend.database.entity.Role;
import com.jimmy_d.notes_backend.dto.UserCreateDto;
import com.jimmy_d.notes_backend.dto.UserInfoDto;
import com.jimmy_d.notes_backend.dto.UserUpdateDto;
import com.jimmy_d.notes_backend.service.UserService;
import com.jimmy_d.notes_backend.test_utils.WithMockCustomUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserRestControllerTestIT extends ControllerTestBase {

    @Autowired
    private UserService userService;

    private UserInfoDto createdUser;

    @BeforeEach
    void setUp() {
        createdUser = userService.createUser(new UserCreateDto(
                "dummy_user", "dummy_pass", "dummy@mail.com", Set.of("USER")
        ));
    }

    // ─── create ───────────────────────────────────────────────

    @Test
    void create_success() throws Exception {
        var dto = new UserCreateDto("new_user", "password123", "new@mail.com", Set.of("USER"));

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("new_user"))
                .andExpect(jsonPath("$.email").value("new@mail.com"));
    }

    @Test
    void create_duplicateUsername_returnsConflict() throws Exception {
        var dto = new UserCreateDto("dummy_user", "password123", "other@mail.com", Set.of("USER"));

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());
    }

    @Test
    void create_invalidEmail_returnsBadRequest() throws Exception {
        var dto = new UserCreateDto("new_user", "password123", "not-an-email", Set.of("USER"));

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    // ─── update ───────────────────────────────────────────────

    @Test
    @WithMockCustomUser
    void update_asOwner_success() throws Exception {
        var dto = new UserUpdateDto(createdUser.id(), "new_john", "new_john@mail.com", null, null);

        mockMvc.perform(patch("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("new_john"));
    }

    @Test
    @WithMockCustomUser(authorities = {Role.ADMIN})
    void update_asAdmin_success() throws Exception {
        var dto = new UserUpdateDto(createdUser.id(), "new_john", "new_john@mail.com", null, null);

        mockMvc.perform(patch("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("new_john"));
    }

    @Test
    @WithMockCustomUser(id = 999L, username = "other_user")
    void update_notOwner_returnsForbidden() throws Exception {
        var dto = new UserUpdateDto(createdUser.id(), "dummy_user", "dummy@mail.com", null, null);

        mockMvc.perform(patch("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }

    // ─── deleteById ───────────────────────────────────────────

    @Test
    @WithMockCustomUser
    void deleteById_asOwner_success() throws Exception {
        mockMvc.perform(delete("/api/v1/users/{id}", createdUser.id()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockCustomUser(authorities = {Role.ADMIN})
    void deleteById_asAdmin_success() throws Exception {
        mockMvc.perform(delete("/api/v1/users/{id}", createdUser.id()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockCustomUser(id = 999L, username = "other_user")
    void deleteById_notOwner_returnsForbidden() throws Exception {
        mockMvc.perform(delete("/api/v1/users/{id}", createdUser.id()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockCustomUser(authorities = {Role.ADMIN})
    void deleteById_notFound_returnsNotFound() throws Exception {
        mockMvc.perform(delete("/api/v1/users/{id}", 999L))
                .andExpect(status().isNotFound());
    }
}