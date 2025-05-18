package com.jimmy_d.notesserver.integration.controller;

import com.jimmy_d.notesserver.database.repository.UserRepository;
import com.jimmy_d.notesserver.dto.UserCreateDto;
import com.jimmy_d.notesserver.dto.UserReadDto;
import com.jimmy_d.notesserver.integration.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@RequiredArgsConstructor
class UserRestControllerIT extends IntegrationTestBase {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;


    //Create test user
    private UserReadDto createRestUser(String username, String email) throws Exception {
        var dto = new UserCreateDto(username, "pass", email, Set.of("USER"));

        var response = mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn();

        return objectMapper.readValue(response.getResponse().getContentAsString(), UserReadDto.class);
    }


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
    void shouldGetUserById() throws Exception {
        var user = createRestUser("anna", "anna@example.com");

        mockMvc.perform(get("/api/v1/users/" + user.id()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("anna@example.com"));
    }

    @Test
    void shouldGetUserByUsername() throws Exception {
        var user = createRestUser("anna", "anna@example.com");
        mockMvc.perform(get("/api/v1/users/by-username/" + user.username()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("anna"));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        var user = createRestUser("kate", "kate@example.com");
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
        var user = createRestUser("alex", "alex@example.com");

        mockMvc.perform(delete("/api/v1/users/" + user.id()))
                .andExpect(status().isNoContent());

        assertTrue(userRepository.findById(user.id()).isEmpty());
    }

    @Test
    void shouldDeleteUserByUsername() throws Exception {
        var user = createRestUser("bob", "bob@example.com");

        mockMvc.perform(delete("/api/v1/users/by-username/" + user.username()))
                .andExpect(status().isNoContent());

        assertTrue(userRepository.findByUsername("bob").isEmpty());
    }

}
