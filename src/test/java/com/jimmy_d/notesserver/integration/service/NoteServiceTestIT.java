package com.jimmy_d.notesserver.integration.service;

import com.jimmy_d.notesserver.dto.NoteFilter;
import com.jimmy_d.notesserver.dto.NotePreviewDto;
import com.jimmy_d.notesserver.dto.NotePreviewFilter;
import com.jimmy_d.notesserver.integration.IntegrationTestBase;
import com.jimmy_d.notesserver.integration.TestFactory;
import com.jimmy_d.notesserver.mapper.UserReadMapper;
import com.jimmy_d.notesserver.service.NoteService;
import com.jimmy_d.notesserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.context.jdbc.Sql;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class NoteServiceTestIT extends IntegrationTestBase {

    private final NoteService noteService;
    private final TestFactory testFactory;
    private final UserReadMapper userReadMapper;
    private final UserService userService;

    static Stream<Arguments> noteFilterCases() {
        return Stream.of(
                Arguments.of(new NoteFilter("dummy_title_1_1", null, null, null), 1),
                Arguments.of(new NoteFilter(null, "dummy_tag_1_1", null, null), 2),
                Arguments.of(new NoteFilter(null, null, "dummy_content_1_1", null), 1),
                Arguments.of(new NoteFilter(null, null, null, 1L), 4),

                Arguments.of(new NoteFilter("dummy_title_1_1", "dummy_tag_1_1", null, null), 1),
                Arguments.of(new NoteFilter("dummy_title_1_1", null, "dummy_content_1_1", null), 1),
                Arguments.of(new NoteFilter("dummy_title_1_1", null, null, 1L), 1),
                Arguments.of(new NoteFilter(null, "dummy_tag_1_1", "dummy_content_1_1", null), 1),
                Arguments.of(new NoteFilter(null, "dummy_tag_1_1", null, 1L), 2),
                Arguments.of(new NoteFilter(null, null, "dummy_content_1_1", 1L), 1),

                Arguments.of(new NoteFilter("dummy_title_1_1", "dummy_tag_1_1", "dummy_content_1_1", null), 1),
                Arguments.of(new NoteFilter("dummy_title_1_1", "dummy_tag_1_1", null, 1L), 1),
                Arguments.of(new NoteFilter("dummy_title_1_1", null, "dummy_content_1_1", 1L), 1),
                Arguments.of(new NoteFilter(null, "dummy_tag_1_1", "dummy_content_1_1", 1L), 1),
                Arguments.of(new NoteFilter("dummy_title_1_1", "dummy_tag_1_1", "dummy_content_1_1", 1L), 1)
        );
    }

    static Stream<Arguments> notePreviewFilterCases() {
        return Stream.of(
                Arguments.of(new NotePreviewFilter("dummy_title_1_1", null, null), 1),
                Arguments.of(new NotePreviewFilter(null, "dummy_tag_1_1", null), 2),
                Arguments.of(new NotePreviewFilter(null, null, "Dummy_user_1"), 4)
        );
    }

    @Test
    void saveShouldSaveNoteSuccessfully() {
        var user = testFactory.createAndSaveUser();
        var noteCreateDto = testFactory.dummyNoteCreateDto(userReadMapper.map(user));
        var savedNote = noteService.save(noteCreateDto);

        assertNotNull(savedNote);
        assertNotNull(savedNote.id());
        assertEquals(savedNote.author().id(), user.getId());
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:sql/test_data.sql")
    void findByIdShouldReturnNoteIfExists() {
        var foundNote = noteService.findById(UUID.fromString("11111111-1111-1111-1111-111111111111"));
        var notFoundNote = noteService.findById(UUID.fromString("11111111-1111-0000-1111-111111111111"));

        assertTrue(foundNote.isPresent());
        assertFalse(notFoundNote.isPresent());
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:sql/test_data.sql")
    void findAllByAuthorShouldReturnNotesList() {
        var user = userService.findByUsername(testFactory.dummyUserCreateDto().username())
                .orElseThrow(() -> new RuntimeException("Test user Not Found"));
        var notes = noteService.findAllByAuthorId(user.id());

        assertNotNull(notes);
        assertTrue(notes.size() >= 2);
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:sql/test_data.sql")
    void findAllByTagShouldReturnNotesList() {
        var notes = noteService.findAllByTag("dummy_tag_1_1");
        assertTrue(notes.size() >= 2);
    }

    @Test
    void deleteNoteByIdShouldDeleteOnceAndReturnFalseSecondTime() {
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
    void getNextNotesShouldReturnNextPageOfNotes() {
        Instant cursor = Instant.parse("2025-01-02T00:00:02Z").plusSeconds(1);

        List<NotePreviewDto> notes = noteService.getNextNotePreview(cursor);

        assertAll("Verify retrieved notes",
                () -> assertNotNull(notes, "Notes should not be null"),
                () -> assertEquals(10, notes.size(), "Should return 10 notes due to page size")
        );
    }

    @ParameterizedTest
    @MethodSource("noteFilterCases")
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:sql/test_data.sql")
    void testNoteFilter(NoteFilter filter, int expectedCount) {
        assertEquals(expectedCount, noteService.findAllByFilter(filter).size());
    }

    @ParameterizedTest
    @MethodSource("notePreviewFilterCases")
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:sql/test_data.sql")
    void testNotePreviewFilter(NotePreviewFilter filter, int expectedCount) {
        assertEquals(expectedCount, noteService.findAllPreviewByFilter(filter, Instant.now()).size());
    }

}
