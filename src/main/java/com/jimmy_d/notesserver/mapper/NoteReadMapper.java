package com.jimmy_d.notesserver.mapper;

import com.jimmy_d.notesserver.database.entity.Note;
import com.jimmy_d.notesserver.dto.NoteReadDto;
import com.jimmy_d.notesserver.dto.UserReadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class NoteReadMapper implements Mapper<Note, NoteReadDto> {

    private final UserReadMapper userReadMapper;

    @Override
    public NoteReadDto map(Note note) {
        UserReadDto author = Optional.ofNullable(note.getAuthor())
                .map(userReadMapper::map)
                .orElse(null);
        return new NoteReadDto(note.getId(), note.getTitle(), note.getTag(), note.getContent(), author);
    }

}
