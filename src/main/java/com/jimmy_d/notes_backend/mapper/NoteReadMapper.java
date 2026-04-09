package com.jimmy_d.notes_backend.mapper;

import com.jimmy_d.notes_backend.database.entity.Note;
import com.jimmy_d.notes_backend.dto.NoteAuthorDto;
import com.jimmy_d.notes_backend.dto.NoteReadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class NoteReadMapper implements Mapper<Note, NoteReadDto> {

    private final NoteAuthorMapper noteAuthorMapper;

    @Override
    public NoteReadDto map(Note note) {
        var author = Optional.ofNullable(note.getAuthor())
                .map(noteAuthorMapper::map)
                .orElse(new NoteAuthorDto(null, "guest"));
        return new NoteReadDto(note.getId(), note.getTitle(), note.getTag(), note.getContent(), author, note.getIsPrivate(), note.getCreatedAt());
    }

}
