package com.jimmy_d.notes_backend.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jimmy_d.notes_backend.database.repository.NoteRepository;
import com.jimmy_d.notes_backend.integration.ControllerTestBase;
import com.jimmy_d.notes_backend.integration.RestTestUtils;
import com.jimmy_d.notes_backend.integration.TestFactory;
import com.jimmy_d.notes_backend.mapper.UserReadMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
class NoteRestControllerIT extends ControllerTestBase {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final TestFactory testFactory;
    private final NoteRepository noteRepository;
    private final UserReadMapper userReadMapper;
    private final RestTestUtils restTestUtils;

    @BeforeEach
    void cleanDatabase() {
        noteRepository.deleteAll();
    }


    @Test
    void shouldCreateNote() throws Exception {
        var user = testFactory.createAndSaveUser();
        var dto = testFactory.dummyNoteCreateDto(userReadMapper.map(user));

        mockMvc.perform(post("/api/v1/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(dto.title()))
                .andExpect(jsonPath("$.tag").value(dto.tag()))
                .andExpect(jsonPath("$.content").value(dto.content()))
                .andExpect(jsonPath("$.author.username").value(user.getUsername()));
    }

    @Test
    void shouldGetNoteById() throws Exception {
        var note = restTestUtils.createNote();

        mockMvc.perform(get("/api/v1/notes/" + note.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(note.id().toString()))
                .andExpect(jsonPath("$.title").value(note.title()));
    }


    @Test
    void shouldGetNotePreviewsByFilter() throws Exception {
        var note = restTestUtils.createNote();

        mockMvc.perform(get("/api/v1/notes/search")
                        .param("tag", note.tag())
                        .param("from", Instant.now().toString())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tag").value(note.tag()));

        mockMvc.perform(get("/api/v1/notes/search")
                        .param("title", note.title())
                        .param("from", Instant.now().toString())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(note.title()));

        mockMvc.perform(get("/api/v1/notes/search")
                        .param("authorId", String.valueOf(note.author().id()))
                        .param("from", Instant.now().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].author").value(note.author().username()));
    }

    @Test
    void shouldDeleteNoteById() throws Exception {
        var note = restTestUtils.createNote();

        mockMvc.perform(delete("/api/v1/notes/" + note.id()))
                .andExpect(status().isNoContent());

        assertThat(noteRepository.findById(note.id())).isEmpty();
    }

}