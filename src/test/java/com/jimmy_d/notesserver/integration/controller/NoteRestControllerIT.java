package com.jimmy_d.notesserver.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jimmy_d.notesserver.database.repository.NoteRepository;
import com.jimmy_d.notesserver.dto.NoteReadDto;
import com.jimmy_d.notesserver.integration.ControllerTestBase;
import com.jimmy_d.notesserver.integration.TestFactory;
import com.jimmy_d.notesserver.mapper.UserReadMapper;
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

    @BeforeEach
    void cleanDatabase() {
        noteRepository.deleteAll();
    }

    private NoteReadDto createNote() throws Exception {
        var user = testFactory.createAndSaveUser();
        var dto = testFactory.dummyNoteCreateDto(userReadMapper.map(user));

        var response = mockMvc.perform(post("/api/v1/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn();

        return objectMapper.readValue(response.getResponse().getContentAsString(), NoteReadDto.class);
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
        var note = createNote();

        mockMvc.perform(get("/api/v1/notes/" + note.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(note.id()))
                .andExpect(jsonPath("$.title").value(note.title()));
    }

    @Test
    void shouldGetAllNotesByTag() throws Exception {
        var note = createNote();

        mockMvc.perform(get("/api/v1/notes/all-by-tag/{tag}", note.tag()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tag").value(note.tag()));
    }

    @Test
    void shouldGetAllNotesByAuthorId() throws Exception {
        var note = createNote();

        mockMvc.perform(get("/api/v1/notes/all-by-author-id/" + note.author().id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].author.id").value(note.author().id()));
    }

    @Test
    void shouldGetNotesByFilter() throws Exception {
        var note = createNote();

        mockMvc.perform(get("/api/v1/notes")
                        .param("title", note.title()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(note.title()));
    }

    @Test
    void shouldDeleteNoteById() throws Exception {
        var note = createNote();

        mockMvc.perform(delete("/api/v1/notes/" + note.id()))
                .andExpect(status().isNoContent());

        assertThat(noteRepository.findById(note.id())).isEmpty();
    }

    @Test
    void shouldDeleteAllByTag() throws Exception {
        var note = createNote();

        mockMvc.perform(delete("/api/v1/notes/all-by-tag/{tag}", note.tag()))
                .andExpect(status().isNoContent());

        assertThat(noteRepository.findAll()).isEmpty();
    }

    @Test
    void shouldDeleteAllByAuthorId() throws Exception {
        var note = createNote();

        mockMvc.perform(delete("/api/v1/notes/all-by-author/" + note.author().id()))
                .andExpect(status().isNoContent());

        assertThat(noteRepository.findAll()).isEmpty();
    }

    @Test
    void shouldGetFreshNotes() throws Exception {
        var note = createNote();
        var from = Instant.now().plusSeconds(60).toString();

        mockMvc.perform(get("/api/v1/notes/fresh")
                        .param("from", from))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(note.id()));
    }
}