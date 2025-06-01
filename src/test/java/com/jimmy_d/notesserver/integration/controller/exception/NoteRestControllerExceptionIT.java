package com.jimmy_d.notesserver.integration.controller.exception;

import com.jimmy_d.notesserver.integration.ControllerTestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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
    void deleteAllByTagShouldReturnNotFoundWhenNoNotesFound() throws Exception {
        String tag = URLEncoder.encode("nonexistent_tag", StandardCharsets.UTF_8);
        mockMvc.perform(delete("/api/v1/notes/all-by-tag/" + tag))
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.errors.note").value("Note not found by tag: [nonexistent_tag]"),
                        jsonPath("$.status").value(404),
                        jsonPath("$.error").value("Not Found")
                );
    }

    @Test
    void deleteAllByAuthorShouldReturnNotFoundWhenNoNotesFound() throws Exception {
        mockMvc.perform(delete("/api/v1/notes/all-by-author/9999"))
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.errors.note").value("Note not found by author id: [9999]"),
                        jsonPath("$.status").value(404),
                        jsonPath("$.error").value("Not Found")
                );
    }

    @Test
    void getNotesShouldReturnBadRequestWhenNoParamsProvided() throws Exception {
        mockMvc.perform(get("/api/v1/notes"))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.status").value(400),
                        jsonPath("$.error").value("Bad Request"),
                        jsonPath("$.errors.query").value("At least one of the next parameters: tag, title, content, or author_id must be provided")
                );
    }
}
