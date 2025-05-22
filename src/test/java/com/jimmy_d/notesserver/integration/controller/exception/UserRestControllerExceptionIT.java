package com.jimmy_d.notesserver.integration.controller.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jimmy_d.notesserver.dto.UserCreateDto;
import com.jimmy_d.notesserver.dto.UserReadDto;
import com.jimmy_d.notesserver.integration.ControllerTestBase;
import com.jimmy_d.notesserver.integration.TestFactory;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RequiredArgsConstructor
class UserRestControllerExceptionIT extends ControllerTestBase {

    private final MockMvc mockMvc;
    private final TestFactory testFactory;
    private final ObjectMapper objectMapper;

    @Test
    void createUserShouldReturnConflictWhenUserAlreadyExists() throws Exception {
        var wrongUsernameDto = testFactory.dummyUserCreateDto();
        testFactory.createAndSaveUser();

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongUsernameDto)))
                .andExpectAll(
                        status().isConflict(),
                        jsonPath("$.status").value(409),
                        jsonPath("$.errors.user").value(String.format("User with username: [%s] already exists", wrongUsernameDto.username())),
                        jsonPath("$.error").value("Conflict")
                );

        var wrongEmailDto = new UserCreateDto(wrongUsernameDto.username() + "dummy", wrongUsernameDto.RawPassword(), wrongUsernameDto.email(), wrongUsernameDto.roles());
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongEmailDto)))
                .andExpectAll(
                        status().isConflict(),
                        jsonPath("$.status").value(409),
                        jsonPath("$.errors.user").value(String.format("User with email: [%s] already exists", wrongEmailDto.email())),
                        jsonPath("$.error").value("Conflict")
                );
    }

    @Test
    void shouldReturn404WhenUserNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/users/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createUserWithInvalidDataShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "testuser",
                                    "email": "invalid-email"
                                }
                                """))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.status").value(400),
                        jsonPath("$.error").value("Bad Request")
                );
    }

    @Test
    void deleteByUsernameShouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        mockMvc.perform(delete("/api/v1/users/by-username/nonexistent"))
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.errors.user").value("User not found by username: [nonexistent]"),
                        jsonPath("$.status").value(404),
                        jsonPath("$.error").value("Not Found")
                );
    }

    @Test
    void deleteByIdShouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        mockMvc.perform(delete("/api/v1/users/-1"))
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.errors.user").value("User not found by id: [-1]"),
                        jsonPath("$.status").value(404),
                        jsonPath("$.error").value("Not Found")
                );
    }

    @Test
    void getByUsernameShouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/v1/users/by-username/nonexistent"))
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.errors.user").value("User not found by username: [nonexistent]"),
                        jsonPath("$.status").value(404),
                        jsonPath("$.error").value("Not Found")
                );
    }

    @Test
    void getByIdShouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/v1/users/-1"))
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.errors.user").value("User not found by id: [-1]"),
                        jsonPath("$.status").value(404),
                        jsonPath("$.error").value("Not Found")
                );
    }

    @Test
    void updateUserShouldReturnConflictWhenUserNotExists() throws Exception {
        var user = testFactory.createAndSaveUser();
        var dto = new UserReadDto(user.getId() + 1, user.getUsername(), user.getEmail(), Set.of("USER"));

        mockMvc.perform(put("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(
                        status().isConflict(),
                        jsonPath("$.errors.update").value("User with id: [" + dto.id() + "] not exists"),
                        jsonPath("$.status").value(409)
                );
    }

}

