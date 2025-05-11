package com.jimmy_d.notesserver.integration.service;

import com.jimmy_d.notesserver.database.entity.Note;
import com.jimmy_d.notesserver.database.entity.User;
import com.jimmy_d.notesserver.dto.NoteCreateDto;
import com.jimmy_d.notesserver.dto.UserCreateDto;
import com.jimmy_d.notesserver.dto.UserReadDto;
import com.jimmy_d.notesserver.integration.IntegrationTestBase;
import com.jimmy_d.notesserver.service.NoteService;
import com.jimmy_d.notesserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;



@RequiredArgsConstructor
class NoteServiceTest extends IntegrationTestBase {

    private final NoteService noteService;
    private final UserService userService;

    private final User DUMMY_USER = new User(1L, "Dummy_user#1", "dummy_#1_pass", "dummy_#1_email");

    private final Note DUMMY_NOTE = new Note(
            1L,
            "dummy_title_#1_1",
            "dummy_tag_#1_1",
            "dummy_content_#1_1",
            DUMMY_USER
    );

    private final NoteCreateDto DUMMY_NOTE_CREATE_DTO = new NoteCreateDto(
            DUMMY_NOTE.getTitle(),
            DUMMY_NOTE.getTag(),
            DUMMY_NOTE.getContent(),
            new UserReadDto(DUMMY_USER.getId(), DUMMY_USER.getUsername(), DUMMY_USER.getEmail())
    );

    private final UserCreateDto DUMMY_USER_CREATE_DTO = new UserCreateDto(
            DUMMY_USER.getUsername(),
            DUMMY_USER.getPassword(),
            DUMMY_USER.getEmail()
    );

    @Test
    void save() {
        userService.save(DUMMY_USER_CREATE_DTO);
        var savedNote = noteService.save(DUMMY_NOTE_CREATE_DTO);
        assertNotNull(savedNote);
        assertNotNull(savedNote.id());
        assertEquals(savedNote.author(), DUMMY_NOTE_CREATE_DTO.author());
    }

    @Test
    void findById() {
        userService.save(DUMMY_USER_CREATE_DTO);
        var savedNote = noteService.save(DUMMY_NOTE_CREATE_DTO);
        assertNotNull(savedNote);
        var foundedNote = noteService.findById(savedNote.id());
        var notFoundedNote = noteService.findById(-1L);
        assertTrue(foundedNote.isPresent());
        assertFalse(notFoundedNote.isPresent());
    }

    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:sql/test_data.sql")
    @Test
    void findAllByAuthor() {
        var allByAuthor = noteService.findAllByAuthor(DUMMY_USER);
        assertNotNull(allByAuthor);
        assertTrue(allByAuthor.size() >= 2);
    }

    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:sql/test_data.sql")
    @Test
    void findAllByTag() {
        var allByTag = noteService.findAllByTag(DUMMY_NOTE.getTag());
        assertTrue(allByTag.size() >= 2);
    }
}