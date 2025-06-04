package com.jimmy_d.notesserver.integration.controller;

import com.jimmy_d.notesserver.database.repository.NoteRepository;
import com.jimmy_d.notesserver.integration.ControllerTestBase;
import com.jimmy_d.notesserver.integration.RestTestUtils;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
public class AdminNoteControllerExceptionIT extends ControllerTestBase {
    private final MockMvc mockMvc;
    private final NoteRepository noteRepository;
    private final RestTestUtils restTestUtils;


    @Test
    void shouldGetAllNotesByTag() throws Exception {
        var note = restTestUtils.createNote();

        mockMvc.perform(get("/api/admin/notes/all-by-tag/{tag}", note.tag()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tag").value(note.tag()));
    }

    @Test
    void shouldGetAllNotesByAuthorId() throws Exception {
        var note = restTestUtils.createNote();

        mockMvc.perform(get("/api/admin/notes/all-by-author-id/" + note.author().id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].author.id").value(note.author().id()));
    }

    @Test
    void shouldDeleteAllByTag() throws Exception {
        var note = restTestUtils.createNote();

        mockMvc.perform(delete("/api/admin/notes/all-by-tag/{tag}", note.tag()))
                .andExpect(status().isNoContent());

        assertThat(noteRepository.findAll()).isEmpty();
    }


    @Test
    void shouldDeleteAllByAuthorId() throws Exception {
        var note = restTestUtils.createNote();

        mockMvc.perform(delete("/api/admin/notes/all-by-author/" + note.author().id()))
                .andExpect(status().isNoContent());

        assertThat(noteRepository.findAll()).isEmpty();
    }

}
