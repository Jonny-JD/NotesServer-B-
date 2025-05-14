package com.jimmy_d.notesserver.integration.service;

import com.jimmy_d.notesserver.integration.IntegrationTestBase;
import com.jimmy_d.notesserver.integration.TestFactory;
import com.jimmy_d.notesserver.mapper.UserReadMapper;
import com.jimmy_d.notesserver.service.NoteService;
import com.jimmy_d.notesserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;


@RequiredArgsConstructor
class NoteServiceTest extends IntegrationTestBase {

    private final NoteService noteService;
    private final TestFactory testFactory;
    private final UserReadMapper userReadMapper;
    private final UserService userService;


    @Test
    void save() {
        var user = testFactory.createAndSaveUser();
        var noteCreateDto = testFactory.dummyNoteCreateDto(userReadMapper.map(user));
        var savedNote = noteService.save(noteCreateDto);

        assertNotNull(savedNote);
        assertNotNull(savedNote.id());
        assertEquals(savedNote.author().id(), user.getId());
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:sql/test_data.sql")
    void findById() {
        var foundNote = noteService.findById(1L);
        var notFoundNote = noteService.findById(-1L);

        assertTrue(foundNote.isPresent());
        assertFalse(notFoundNote.isPresent());
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:sql/test_data.sql")
    void findAllByAuthor() {
        var user = userService.findByUsername(testFactory.dummyUserCreateDto().username()).orElseThrow(() -> new RuntimeException("Test user Not Found"));
        var notes = noteService.findAllByAuthor(user);

        assertNotNull(notes);
        assertTrue(notes.size() >= 2);
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:sql/test_data.sql")
    void findAllByTag() {
        var notes = noteService.findAllByTag("dummy_tag_#1_1");
        assertTrue(notes.size() >= 2);
    }

    @Test
    void deleteNoteById() {
        var user = testFactory.createAndSaveUser();
        var noteCreateDto = testFactory.dummyNoteCreateDto(userReadMapper.map(user));
        var savedNote = noteService.save(noteCreateDto);

        var firstDelete = noteService.deleteById(savedNote.id());
        var secondDelete = noteService.deleteById(savedNote.id());

        assertTrue(firstDelete);
        assertFalse(secondDelete);
    }
}