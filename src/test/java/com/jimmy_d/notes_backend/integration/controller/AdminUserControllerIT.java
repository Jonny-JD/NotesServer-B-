package com.jimmy_d.notes_backend.integration.controller;

import com.jimmy_d.notes_backend.database.repository.UserRepository;
import com.jimmy_d.notes_backend.integration.ControllerTestBase;
import com.jimmy_d.notes_backend.integration.RestTestUtils;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
public class AdminUserControllerIT extends ControllerTestBase {
    private final MockMvc mockMvc;
    private final UserRepository userRepository;
    private final RestTestUtils restTestUtils;

    @BeforeEach
    void cleanDatabase() {
        userRepository.deleteAll();
    }

    @Test
    void shouldGetUserById() throws Exception {
        var user = restTestUtils.createRestUser("anna", "anna@example.com");

        mockMvc.perform(get("/api/admin/users/" + user.id()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("anna@example.com"));
    }

    @Test
    void shouldGetUserByUsername() throws Exception {
        var user = restTestUtils.createRestUser("anna", "anna@example.com");
        mockMvc.perform(get("/api/admin/users/by-username/" + user.username()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("anna"));
    }


    @Test
    void shouldDeleteUserByUsername() throws Exception {
        var user = restTestUtils.createRestUser("anna", "anna@example.com");

        mockMvc.perform(delete("/api/admin/users/by-username/" + user.username()))
                .andExpect(status().isNoContent());

        assertTrue(userRepository.findByUsername("bob").isEmpty());
    }
}
