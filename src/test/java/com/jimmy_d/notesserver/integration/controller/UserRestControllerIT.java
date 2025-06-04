package com.jimmy_d.notesserver.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jimmy_d.notesserver.database.repository.UserRepository;
import com.jimmy_d.notesserver.dto.UserCreateDto;
import com.jimmy_d.notesserver.dto.UserReadDto;
import com.jimmy_d.notesserver.integration.ControllerTestBase;
import com.jimmy_d.notesserver.integration.RestTestUtils;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RequiredArgsConstructor
class UserRestControllerIT extends ControllerTestBase {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final RestTestUtils restTestUtils;


    @BeforeEach
    void cleanDatabase() {
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateUser() throws Exception {
        var dto = new UserCreateDto("john", "password", "john@example.com", Set.of("USER"));

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("john"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }

    @Test
    void shouldUpdateUser() throws Exception {
        var user = restTestUtils.createRestUser("anna", "anna@example.com");
        var updated = new UserReadDto(user.id(), "kate", "new@example.com", Set.of("USER", "ADMIN"));

        mockMvc.perform(put("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("new@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.roles").isArray());
    }

    @Test
    void shouldDeleteUserById() throws Exception {
        var user = restTestUtils.createRestUser("anna", "anna@example.com");

        mockMvc.perform(delete("/api/v1/users/" + user.id()))
                .andExpect(status().isNoContent());

        assertTrue(userRepository.findById(user.id()).isEmpty());
    }

}

