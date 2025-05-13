package com.jimmy_d.notesserver.mapper;

import com.jimmy_d.notesserver.database.entity.Note;
import com.jimmy_d.notesserver.database.entity.User;
import com.jimmy_d.notesserver.database.repository.UserRepository;
import com.jimmy_d.notesserver.dto.NoteCreateDto;
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
    }

    private User getAuthor(com.jimmy_d.notesserver.dto.UserReadDto author) {
        var foundedAuthor = userRepository.findById(author.id());
        return foundedAuthor.orElse(null);
    }
}
