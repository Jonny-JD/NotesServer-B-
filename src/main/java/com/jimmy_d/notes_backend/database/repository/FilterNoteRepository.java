package com.jimmy_d.notes_backend.database.repository;

import com.jimmy_d.notes_backend.database.entity.Note;
import com.jimmy_d.notes_backend.dto.NoteFilter;
import com.jimmy_d.notes_backend.dto.NotePreviewFilter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface FilterNoteRepository {
    List<Note> findAllByFilter(NoteFilter filter);

    List<Note> findAllPreviewByFilter(@Param("filter") NotePreviewFilter filter, @Param("cursor") Instant cursor, Pageable pageable);
}
