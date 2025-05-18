package com.jimmy_d.notesserver.database.repository;

import com.jimmy_d.notesserver.database.entity.Note;
import com.jimmy_d.notesserver.dto.NoteFilter;

import java.util.List;

public interface FilterNoteRepository {
    List<Note> findAllByFilter(NoteFilter filter);
}
