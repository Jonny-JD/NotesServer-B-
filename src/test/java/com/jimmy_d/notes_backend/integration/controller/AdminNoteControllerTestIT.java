package com.jimmy_d.notes_backend.integration.controller;

import com.jimmy_d.notes_backend.database.entity.Role;
import com.jimmy_d.notes_backend.dto.*;
import com.jimmy_d.notes_backend.service.NoteService;
import com.jimmy_d.notes_backend.service.UserService;
import com.jimmy_d.notes_backend.test_utils.WithMockCustomUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AdminNoteControllerTestIT extends ControllerTestBase {

    @Autowired
    private NoteService noteService;

    @Autowired
    private UserService userService;

    private UserInfoDto createdUser;

    @BeforeEach
    void setUp() {
        createdUser = userService.createUser(new UserCreateDto(
                "jane", "password123", "jane@mail.com", Set.of("USER")
        ));
        var author = new UserDetailsDto(createdUser.id(), createdUser.username(), createdUser.email(), Set.of("USER"));
        noteService.save(new NoteCreateDto("Test title", "test-tag", "Test content", author, false));
    }

    // ─── getAllByTag ───────────────────────────────────────────

    @Test
    @WithMockCustomUser(authorities = {Role.ADMIN})
    void getAllByTag_success() throws Exception {
        mockMvc.perform(get("/api/v1/admin/notes/all-by-tag/test-tag"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tag").value("test-tag"));
    }

    @Test
    @WithMockCustomUser(authorities = {Role.ADMIN})
    void getAllByTag_noNotes_returnsEmptyList() throws Exception {
        mockMvc.perform(get("/api/v1/admin/notes/all-by-tag/nonexistent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @WithMockCustomUser(authorities = {Role.USER})
    void getAllByTag_notAdmin_returnsForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/admin/notes/all-by-tag/test-tag"))
                .andExpect(status().isForbidden());
    }

    // ─── getAllByAuthor ────────────────────────────────────────

    @Test
    @WithMockCustomUser(authorities = {Role.ADMIN})
    void getAllByAuthor_success() throws Exception {
        mockMvc.perform(get("/api/v1/admin/notes/all-by-author/{id}", createdUser.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].author.username").value("jane"));
    }

    // ─── deleteAllByTag ───────────────────────────────────────

    @Test
    @WithMockCustomUser(authorities = {Role.ADMIN})
    void deleteAllByTag_success() throws Exception {
        mockMvc.perform(delete("/api/v1/admin/notes/all-by-tag/test-tag"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockCustomUser(authorities = {Role.USER})
    void deleteAllByTag_notAdmin_returnsForbidden() throws Exception {
        mockMvc.perform(delete("/api/v1/admin/notes/all-by-tag/test-tag"))
                .andExpect(status().isForbidden());
    }

    // ─── deleteAllByAuthor ────────────────────────────────────

    @Test
    @WithMockCustomUser(authorities = {Role.ADMIN})
    void deleteAllByAuthor_success() throws Exception {
        mockMvc.perform(delete("/api/v1/admin/notes/all-by-author/{id}", createdUser.id()))
                .andExpect(status().isNoContent());
    }
}