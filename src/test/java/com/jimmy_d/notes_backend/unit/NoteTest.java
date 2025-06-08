package com.jimmy_d.notes_backend.unit;

import com.jimmy_d.notes_backend.database.entity.Note;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class NoteTest {

    @Test
    void equalsTests() {
        Note note1 = Note.builder().id(UUID.fromString("11111111-1111-1111-1111-111111111111")).build();
        Note note2 = Note.builder().id(UUID.fromString("11111111-1111-1111-1111-111111111111")).build();
        Note note3 = Note.builder().id(UUID.fromString("22222222-2222-2222-2222-222222222222")).build();
        Note noteNull1 = Note.builder().build();
        Note noteNull2 = Note.builder().build();

        assertAll("equals tests",
                () -> assertEquals(note1, note1, "same instance should be equal"),
                () -> assertNotEquals(null, note1, "should not equal null"),
                () -> assertNotEquals(new Object(), note1, "different class should not be equal"),
                () -> assertNotEquals(note2, note3, "different ids should not be equal"),
                () -> assertEquals(note1, note2, "same ids should be equal"),
                () -> assertNotEquals(noteNull1, noteNull2, "both ids null should not be equal")
        );
    }

    @Test
    void hashCodeTests() {
        Note noteWithId = Note.builder().id(UUID.fromString("11111111-1111-1111-1111-111111111111")).build();
        Note noteWithoutId = Note.builder().build();
        int initialHash = noteWithId.hashCode();

        assertAll("hashCode tests",
                () -> assertEquals(Note.class.hashCode(), noteWithoutId.hashCode(), "hashCode without id"),
                () -> assertEquals(initialHash, noteWithId.hashCode(), "hashCode consistent for same instance")
        );
    }
}
