package com.jimmy_d.notesserver.mapper;

import com.jimmy_d.notesserver.database.entity.Note;
import com.jimmy_d.notesserver.dto.NoteAuthorDto;
import com.jimmy_d.notesserver.dto.NotePreviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class NotePreviewMapper implements Mapper<Note, NotePreviewDto> {

    private final NoteAuthorMapper noteAuthorMapper;

    @Override
    public NotePreviewDto map(Note note) {
        var author = Optional.ofNullable(note.getAuthor())
                .map(noteAuthorMapper::map)
                .map(NoteAuthorDto::username)
                .orElse(null);
        return new NotePreviewDto(note.getId(), note.getTitle(), note.getTag(), author, note.getCreatedAt());
    }

}
