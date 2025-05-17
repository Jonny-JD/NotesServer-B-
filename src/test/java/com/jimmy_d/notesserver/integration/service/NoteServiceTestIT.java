package com.jimmy_d.notesserver.integration.service;

import com.jimmy_d.notesserver.dto.NoteReadDto;
import com.jimmy_d.notesserver.integration.IntegrationTestBase;
import com.jimmy_d.notesserver.integration.TestFactory;
import com.jimmy_d.notesserver.mapper.UserReadMapper;
import com.jimmy_d.notesserver.service.NoteService;
import com.jimmy_d.notesserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@RequiredArgsConstructor
class NoteServiceTestIT extends IntegrationTestBase {

    private final NoteService noteService;
    private final TestFactory testFactory;
    private final UserReadMapper userReadMapper;
    private final UserService userService;


    @Test
    void save_shouldSaveNoteSuccessfully() {
        var user = testFactory.createAndSaveUser();
        var noteCreateDto = testFactory.dummyNoteCreateDto(userReadMapper.map(user));
        var savedNote = noteService.save(noteCreateDto);

        assertNotNull(savedNote);
        assertNotNull(savedNote.id());
        assertEquals(savedNote.author().id(), user.getId());
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:sql/test_data.sql")
    void findById_shouldReturnNoteIfExists() {
        var foundNote = noteService.findById(1L);
        var notFoundNote = noteService.findById(-1L);

        assertTrue(foundNote.isPresent());
        assertFalse(notFoundNote.isPresent());
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:sql/test_data.sql")
    void findAllByAuthor_shouldReturnNotesList() {
        var user = userService.findByUsername(testFactory.dummyUserCreateDto().username())
                .orElseThrow(() -> new RuntimeException("Test user Not Found"));
        var notes = noteService.findAllByAuthor(user);

        assertNotNull(notes);
        assertTrue(notes.size() >= 2);
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:sql/test_data.sql")
    void findAllByTag_shouldReturnNotesList() {
        var notes = noteService.findAllByTag("dummy_tag_#1_1");
        assertTrue(notes.size() >= 2);
    }

    @Test
    void deleteNoteById_shouldDeleteOnceAndReturnFalseSecondTime() {
        var user = testFactory.createAndSaveUser();
        var noteCreateDto = testFactory.dummyNoteCreateDto(userReadMapper.map(user));
        var savedNote = noteService.save(noteCreateDto);

        var firstDelete = noteService.deleteById(savedNote.id());
        var secondDelete = noteService.deleteById(savedNote.id());

        assertTrue(firstDelete);
        assertFalse(secondDelete);
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:sql/test_data.sql")
    void getNextNotes_shouldReturnNextPageOfNotes() {
        Instant cursor = Instant.parse("2025-01-02T00:00:02Z").plusSeconds(1);

        List<NoteReadDto> notes = noteService.getNextNotes(cursor);

        assertAll("Verify retrieved notes",
                () -> assertNotNull(notes, "Notes should not be null"),
                () -> assertEquals(10, notes.size(), "Should return 10 notes due to page size")
        );
    }
}
