package com.jimmy_d.notes_backend.mapper;

import com.jimmy_d.notes_backend.database.entity.Note;
import com.jimmy_d.notes_backend.database.entity.User;
import com.jimmy_d.notes_backend.database.repository.UserRepository;
import com.jimmy_d.notes_backend.dto.NoteCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NoteCreateMapper implements Mapper<NoteCreateDto, Note> {

    private final UserRepository userRepository;

    @Override
    public Note map(NoteCreateDto noteCreateDto) {
        Note note = new Note();
        copy(noteCreateDto, note);
        return note;
    }

    private void copy(NoteCreateDto noteCreateDto, Note note) {
        note.setTag(noteCreateDto.tag());
        note.setTitle(noteCreateDto.title());
        note.setContent(noteCreateDto.content());
        note.setAuthor(getAuthor(noteCreateDto.author()));
        note.setIsPrivate(noteCreateDto.isPrivate());
    }

    private User getAuthor(com.jimmy_d.notes_backend.dto.UserReadDto author) {
        var foundedAuthor = userRepository.findById(author.id());
        return foundedAuthor.orElse(null);
    }
}
