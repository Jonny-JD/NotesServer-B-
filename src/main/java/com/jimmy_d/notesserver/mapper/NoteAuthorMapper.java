package com.jimmy_d.notesserver.mapper;

import com.jimmy_d.notesserver.database.entity.User;
import com.jimmy_d.notesserver.dto.NoteAuthorDto;
import org.springframework.stereotype.Component;

@Component
public class NoteAuthorMapper implements Mapper<User, NoteAuthorDto> {

    @Override
    public NoteAuthorDto map(User user) {
        return new NoteAuthorDto(user.getId(),
                user.getUsername());
    }
}
