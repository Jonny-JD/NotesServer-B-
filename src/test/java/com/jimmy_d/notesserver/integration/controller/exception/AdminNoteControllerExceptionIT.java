package com.jimmy_d.notesserver.integration.controller.exception;

import com.jimmy_d.notesserver.integration.ControllerTestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
public class AdminNoteControllerExceptionIT extends ControllerTestBase {
    private final MockMvc mockMvc;

    @Test
    void deleteAllByTagShouldReturnNotFoundWhenNoNotesFound() throws Exception {
        String tag = URLEncoder.encode("nonexistent_tag", StandardCharsets.UTF_8);
        mockMvc.perform(delete("/api/admin/notes/all-by-tag/" + tag))
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.errors.note").value("Note not found by tag: [nonexistent_tag]"),
                        jsonPath("$.status").value(404),
                        jsonPath("$.error").value("Not Found")
                );
    }

    @Test
    void deleteAllByAuthorShouldReturnNotFoundWhenNoNotesFound() throws Exception {
        mockMvc.perform(delete("/api/admin/notes/all-by-author/9999"))
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.errors.note").value("Note not found by author id: [9999]"),
                        jsonPath("$.status").value(404),
                        jsonPath("$.error").value("Not Found")
                );
    }
}
