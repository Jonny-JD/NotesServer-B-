package com.jimmy_d.notesserver.integration.controller.exception;

import com.jimmy_d.notesserver.integration.ControllerTestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
public class AdminUserControllerExceptionIT extends ControllerTestBase {
    private final MockMvc mockMvc;


    @Test
    void shouldReturn404WhenUserNotFound() throws Exception {
        mockMvc.perform(get("/api/admin/users/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteByUsernameShouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        mockMvc.perform(delete("/api/admin/users/by-username/nonexistent"))
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.errors.user").value("User not found by username: [nonexistent]"),
                        jsonPath("$.status").value(404),
                        jsonPath("$.error").value("Not Found")
                );
    }

    @Test
    void getByUsernameShouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/admin/users/by-username/nonexistent"))
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.errors.user").value("User not found by username: [nonexistent]"),
                        jsonPath("$.status").value(404),
                        jsonPath("$.error").value("Not Found")
                );
    }

    @Test
    void getByIdShouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/admin/users/-1"))
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.errors.user").value("User not found by id: [-1]"),
                        jsonPath("$.status").value(404),
                        jsonPath("$.error").value("Not Found")
                );
    }
}
