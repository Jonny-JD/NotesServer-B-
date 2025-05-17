package com.jimmy_d.notesserver.integration.controller.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jimmy_d.notesserver.dto.UserReadDto;
import com.jimmy_d.notesserver.integration.IntegrationTestBase;
import com.jimmy_d.notesserver.integration.TestFactory;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RequiredArgsConstructor
class UserRestControllerExceptionIT extends IntegrationTestBase {

    private final MockMvc mockMvc;
    private final TestFactory testFactory;
    private final ObjectMapper objectMapper;

    @Test
    void createUser_shouldReturnBadRequest_whenUserAlreadyExists() throws Exception {
        var dto = testFactory.dummyUserCreateDto();
        testFactory.createAndSaveUser();

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath(("$.errors.user")).value(String.format("User with username: [%s] already exists", dto.username())))
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

    @Test
    void deleteByUsername_shouldReturnNotFound_whenUserDoesNotExist() throws Exception {
        mockMvc.perform(delete("/api/v1/users/by-username/nonexistent"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(("$.errors.user")).value("User not found by username: [nonexistent]"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void deleteById_shouldReturnNotFound_whenUserDoesNotExist() throws Exception {
        mockMvc.perform(delete("/api/v1/users/-1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(("$.errors.user")).value("User not found by id: [-1]"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void getByUsername_shouldReturnNotFound_whenUserDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/v1/users/by-username/nonexistent"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(("$.errors.user")).value("User not found by username: [nonexistent]"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void getById_shouldReturnNotFound_whenUserDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/v1/users/-1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(("$.errors.user")).value("User not found by id: [-1]"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void updateUser_shouldReturnConflict_whenIdsDoNotMatch() throws Exception {
        var user = testFactory.createAndSaveUser();
        var dto = new UserReadDto(user.getId() + 1, user.getUsername(), user.getEmail(), Set.of("USER"));

        mockMvc.perform(put("/api/v1/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errors.update").value("ID in path and body must match"))
                .andExpect(jsonPath("$.status").value(409));
    }
}
