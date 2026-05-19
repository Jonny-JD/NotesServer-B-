package com.jimmy_d.notes_backend.integration.controller;

import com.jimmy_d.notes_backend.database.entity.Role;
import com.jimmy_d.notes_backend.dto.UserCreateDto;
import com.jimmy_d.notes_backend.dto.UserDetailsDto;
import com.jimmy_d.notes_backend.dto.UserInfoDto;
import com.jimmy_d.notes_backend.service.UserService;
import com.jimmy_d.notes_backend.test_utils.WithMockCustomUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AdminUserControllerTestIT extends ControllerTestBase {

    @Autowired
    private UserService userService;

    private UserInfoDto createdUser;

    @BeforeEach
    void setUp() {
        createdUser = userService.createUser(new UserCreateDto(
                "john", "password123", "john@mail.com", Set.of("USER")
        ));
    }

    // ─── getById ──────────────────────────────────────────────

    @Test
    @WithMockCustomUser(username = "john", authorities = {Role.ADMIN})
    void getById_success() throws Exception {
        mockMvc.perform(get("/api/v1/admin/users/{id}", createdUser.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john"))
                .andExpect(jsonPath("$.roles").isNotEmpty());
    }

    @Test
    @WithMockCustomUser(authorities = {Role.USER})
    void getById_notAdmin_returnsForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/admin/users/{id}", createdUser.id()))
                .andExpect(status().isForbidden());
    }

    // ─── getUserByUsername ────────────────────────────────────

    @Test
    @WithMockCustomUser(username = "john", authorities = {Role.ADMIN})
    void getUserByUsername_success() throws Exception {
        mockMvc.perform(get("/api/v1/admin/users/username/john"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john"));
    }

    @Test
    @WithMockCustomUser(authorities = {Role.ADMIN})
    void getUserByUsername_notFound_returnsNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/admin/users/username/ghost_user"))
                .andExpect(status().isNotFound());
    }

    // ─── deleteByUsername ─────────────────────────────────────

    @Test
    @WithMockCustomUser(username = "john", authorities = {Role.ADMIN})
    void deleteByUsername_success() throws Exception {
        mockMvc.perform(delete("/api/v1/admin/users/username/john"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockCustomUser(authorities = {Role.ADMIN})
    void deleteByUsername_notFound_returnsNotFound() throws Exception {
        mockMvc.perform(delete("/api/v1/admin/users/username/ghost_user"))
                .andExpect(status().isNotFound());
    }

    // ─── deleteById ───────────────────────────────────────────

    @Test
    @WithMockCustomUser(authorities = {Role.ADMIN})
    void deleteById_success() throws Exception {
        mockMvc.perform(delete("/api/v1/admin/users/id/{id}", createdUser.id()))
                .andExpect(status().isNoContent());
    }

    // ─── addUserRole ──────────────────────────────────────────

    @Test
    @WithMockCustomUser(authorities = {Role.ADMIN})
    void addUserRole_success() throws Exception {
        var details = new UserDetailsDto(createdUser.id(), createdUser.username(), createdUser.email(), Set.of("USER"));

        mockMvc.perform(patch("/api/v1/admin/users/roles/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(details))
                        .param("role", "ADMIN"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockCustomUser(authorities = {Role.ADMIN})
    void addUserRole_alreadyExists_returnsConflict() throws Exception {
        var details = new UserDetailsDto(createdUser.id(), createdUser.username(), createdUser.email(), Set.of("USER"));

        mockMvc.perform(patch("/api/v1/admin/users/roles/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(details))
                        .param("role", "USER"))
                .andExpect(status().isConflict());
    }

    // ─── removeUserRole ───────────────────────────────────────

    @Test
    @WithMockCustomUser(authorities = {Role.ADMIN})
    void removeUserRole_success() throws Exception {
        var details = new UserDetailsDto(createdUser.id(), createdUser.username(), createdUser.email(), Set.of("USER"));

        mockMvc.perform(patch("/api/v1/admin/users/roles/remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(details))
                        .param("role", "USER"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockCustomUser(authorities = {Role.ADMIN})
    void removeUserRole_notFound_returnsNotFound() throws Exception {
        var details = new UserDetailsDto(createdUser.id(), createdUser.username(), createdUser.email(), Set.of("USER"));

        mockMvc.perform(patch("/api/v1/admin/users/roles/remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(details))
                        .param("role", "ADMIN"))
                .andExpect(status().isNotFound());
    }
}