package com.jimmy_d.notes_backend.integration.controller.exception;

import com.jimmy_d.notes_backend.integration.ControllerTestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RequiredArgsConstructor
class NoteRestControllerExceptionIT extends ControllerTestBase {

    private final MockMvc mockMvc;

    @Test
    void getByIdShouldReturnNotFoundWhenNoteDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/v1/notes/11111111-1111-1111-0000-111111111111"))
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.errors.note").value("Note not found by id: [11111111-1111-1111-0000-111111111111]"),
                        jsonPath("$.status").value(404),
                        jsonPath("$.error").value("Not Found")
                );
    }

    @Test
    void deleteByIdShouldReturnNotFoundWhenNoteDoesNotExist() throws Exception {
        mockMvc.perform(delete("/api/v1/notes/11111111-1111-1111-0000-111111111111"))
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.errors.note").value("Note not found by id: [11111111-1111-1111-0000-111111111111]"),
                        jsonPath("$.status").value(404),
                        jsonPath("$.error").value("Not Found")
                );
    }

    @Test
    void searchShouldReturnBadRequestWhenAllParamsAreMissing() throws Exception {
        mockMvc.perform(get("/api/v1/notes/search"))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.errors.parameter").value("Required request parameter 'from' for method parameter type Instant is not present"),
                        jsonPath("$.status").value(400),
                        jsonPath("$.error").value("Bad Request")
                );
    }

}
