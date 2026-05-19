package com.jimmy_d.notes_backend.mapper;

import com.jimmy_d.notes_backend.database.entity.User;
import com.jimmy_d.notes_backend.dto.UserInfoDto;
import org.springframework.stereotype.Component;

@Component
public class UserInfoMapper implements Mapper<User, UserInfoDto> {

    @Override
    public UserInfoDto map(User user) {
        return new UserInfoDto(user.getId(),
                user.getUsername(),
                user.getEmail());
    }

    public User map(UserInfoDto userInfoDto) {
        var user = new  User();
        user.setId(userInfoDto.id());
        user.setUsername(userInfoDto.username());
        user.setEmail(userInfoDto.email());
        return user;

    }
}
