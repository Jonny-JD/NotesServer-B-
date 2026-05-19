package com.jimmy_d.notes_backend.integration.service;

import com.jimmy_d.notes_backend.dto.*;
import com.jimmy_d.notes_backend.exceptions.rest.NoteNotFoundException;
import com.jimmy_d.notes_backend.integration.IntegrationTestBase;
import com.jimmy_d.notes_backend.service.NoteService;
import com.jimmy_d.notes_backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class NoteServiceTestIT extends IntegrationTestBase {

    @Autowired
    private NoteService noteService;

    @Autowired
    private UserService userService;

    private UserInfoDto createdUser;
    private NoteReadDto createdNote;

    @BeforeEach
    void setUp() {
        createdUser = userService.createUser(new UserCreateDto(
                "john",
                "password123",
                "jimmy@mail.com",
                Set.of("USER")
        ));

        var author = new UserDetailsDto(createdUser.id(), createdUser.username(), createdUser.email(), Set.of("USER"));

        createdNote = noteService.save(new NoteCreateDto(
                "Test title",
                "test-tag",
                "Test content",
                author,
                false
        ));
    }

    // ─── save ─────────────────────────────────────────────────

    @Test
    void save_success() {
        assertThat(createdNote).isNotNull();
        assertThat(createdNote.title()).isEqualTo("Test title");
        assertThat(createdNote.tag()).isEqualTo("test-tag");
        assertThat(createdNote.content()).isEqualTo("Test content");
        assertThat(createdNote.isPrivate()).isFalse();
        assertThat(createdNote.author().username()).isEqualTo("john");
    }

    @Test
    void save_withNullTitleAndTag_success() {
        var author = new UserDetailsDto(createdUser.id(), createdUser.username(), createdUser.email(), Set.of("USER"));
        var note = noteService.save(new NoteCreateDto("", "", "Content only", author, false));

        assertThat(note.title()).isNull();
        assertThat(note.tag()).isNull();
        assertThat(note.content()).isEqualTo("Content only");
    }

    // ─── findById ─────────────────────────────────────────────

    @Test
    void findById_success() {
        var found = noteService.findById(createdNote.id());

        assertThat(found.id()).isEqualTo(createdNote.id());
        assertThat(found.title()).isEqualTo("Test title");
        assertThat(found.content()).isEqualTo("Test content");
    }

    @Test
    void findById_notFound_throwsNoteNotFoundException() {
        var randomId = UUID.randomUUID();

        assertThatThrownBy(() -> noteService.findById(randomId))
                .isInstanceOf(NoteNotFoundException.class);
    }

    // ─── updateNote ───────────────────────────────────────────

    @Test
    void updateNote_success() {
        var dto = new NoteUpdateDto("Updated title", "new-tag", "Updated content", true);
        var updated = noteService.updateNote(createdNote.id(), dto);

        assertThat(updated.title()).isEqualTo("Updated title");
        assertThat(updated.tag()).isEqualTo("new-tag");
        assertThat(updated.content()).isEqualTo("Updated content");
        assertThat(updated.isPrivate()).isTrue();
    }

    @Test
    void updateNote_emptyTitleAndTag_setsNull() {
        var dto = new NoteUpdateDto("", "", "Updated content", false);
        var updated = noteService.updateNote(createdNote.id(), dto);

        assertThat(updated.title()).isNull();
        assertThat(updated.tag()).isNull();
    }

    @Test
    void updateNote_notFound_throwsNoteNotFoundException() {
        var randomId = UUID.randomUUID();
        var dto = new NoteUpdateDto("Title", "tag", "Content", false);

        assertThatThrownBy(() -> noteService.updateNote(randomId, dto))
                .isInstanceOf(NoteNotFoundException.class);
    }

    // ─── findAllByAuthorId ────────────────────────────────────

    @Test
    void findAllByAuthorId_success() {
        var notes = noteService.findAllByAuthorId(createdUser.id());

        assertThat(notes).hasSize(1);
        assertThat(notes.getFirst().author().username()).isEqualTo("john");
    }

    @Test
    void findAllByAuthorId_noNotes_returnsEmptyList() {
        var otherUser = userService.createUser(new UserCreateDto(
                "other_user", "pass123", "other@mail.com", Set.of("USER")
        ));

        var notes = noteService.findAllByAuthorId(otherUser.id());

        assertThat(notes).isEmpty();
    }

    // ─── findAllByTag ─────────────────────────────────────────

    @Test
    void findAllByTag_success() {
        var notes = noteService.findAllByTag("test-tag");

        assertThat(notes).hasSize(1);
        assertThat(notes.getFirst().tag()).isEqualTo("test-tag");
    }

    @Test
    void findAllByTag_noNotes_returnsEmptyList() {
        var notes = noteService.findAllByTag("nonexistent-tag");

        assertThat(notes).isEmpty();
    }

    // ─── findAllPreviewByFilter ───────────────────────────────

    @Test
    void findAllPreviewByFilter_byAuthor_success() {
        var filter = new NotePreviewFilter(null, null, "john");
        var results = noteService.findAllPreviewByFilter(filter, Instant.now().plusSeconds(60));

        assertThat(results).hasSize(1);
        assertThat(results.getFirst().author()).isEqualTo("john");
    }

    @Test
    void findAllPreviewByFilter_byTag_success() {
        var filter = new NotePreviewFilter(null, "test-tag", null);
        var results = noteService.findAllPreviewByFilter(filter, Instant.now().plusSeconds(60));

        assertThat(results).hasSize(1);
        assertThat(results.getFirst().tag()).isEqualTo("test-tag");
    }

    @Test
    void findAllPreviewByFilter_cursorAfterCreation_returnsEmpty() {
        var filter = new NotePreviewFilter(null, null, "john");
        var results = noteService.findAllPreviewByFilter(filter, Instant.now().minusSeconds(60));

        assertThat(results).isEmpty();
    }

    @Test
    void findAllPreviewByFilter_noMatch_returnsEmptyList() {
        var filter = new NotePreviewFilter(null, "nonexistent-tag", null);
        var results = noteService.findAllPreviewByFilter(filter, Instant.EPOCH);

        assertThat(results).isEmpty();
    }

    // ─── deleteById ───────────────────────────────────────────

    @Test
    void deleteById_success() {
        noteService.deleteById(createdNote.id());
        var noteId = createdNote.id();
        assertThatThrownBy(() -> noteService.findById(noteId))
                .isInstanceOf(NoteNotFoundException.class);
    }

    @Test
    void deleteById_notFound_throwsNoteNotFoundException() {
        var randomId = UUID.randomUUID();

        assertThatThrownBy(() -> noteService.deleteById(randomId))
                .isInstanceOf(NoteNotFoundException.class);
    }

    // ─── deleteAllByTag ───────────────────────────────────────

    @Test
    void deleteAllByTag_success() {
        noteService.deleteAllByTag("test-tag");

        var notes = noteService.findAllByTag("test-tag");
        assertThat(notes).isEmpty();
    }

    // ─── deleteAllByAuthor ────────────────────────────────────

    @Test
    void deleteAllByAuthor_success() {
        noteService.deleteAllByAuthor(createdUser.id());

        var notes = noteService.findAllByAuthorId(createdUser.id());
        assertThat(notes).isEmpty();
    }
}