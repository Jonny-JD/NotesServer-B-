package com.jimmy_d.notesserver.mapper;

import com.jimmy_d.notesserver.database.entity.User;
import com.jimmy_d.notesserver.dto.UserReadDto;
import org.springframework.stereotype.Component;

@Component
public class UserReadMapper implements Mapper<User, UserReadDto> {

    @Override
    public UserReadDto map(User user) {
        return new UserReadDto(user.getId(), user.getUsername(), user.getEmail());
    }
}
