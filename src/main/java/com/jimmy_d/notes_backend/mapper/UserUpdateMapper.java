package com.jimmy_d.notes_backend.mapper;

import com.jimmy_d.notes_backend.database.entity.User;
import com.jimmy_d.notes_backend.dto.UserUpdateDto;
import org.springframework.stereotype.Component;

@Component
public class UserUpdateMapper implements Mapper<User, UserUpdateDto> {

    @Override
    public UserUpdateDto map(User user) {
        return new UserUpdateDto(user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getPassword());
    }

    public User map(UserUpdateDto userUpdateDto) {
        var user = new User();
        user.setId(userUpdateDto.id());
        user.setUsername(userUpdateDto.username());
        user.setEmail(userUpdateDto.email());
        return user;

    }
}
