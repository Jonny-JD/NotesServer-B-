package com.jimmy_d.notes_backend.mapper;

import com.jimmy_d.notes_backend.database.entity.Role;
import com.jimmy_d.notes_backend.database.entity.User;
import com.jimmy_d.notes_backend.dto.UserReadDto;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserReadMapper implements Mapper<User, UserReadDto> {

    @Override
    public UserReadDto map(User user) {
        return new UserReadDto(user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles()
                        .stream()
                        .map(Enum::name)
                        .collect(Collectors.toSet()));
    }

    public User map(UserReadDto userReadDto) {
        return User.builder()
                .id(userReadDto.id())
                .username(userReadDto.username())
                .email(userReadDto.email())
                .roles(userReadDto.roles()
                        .stream()
                        .map(Role::valueOf)
                        .collect(Collectors.toSet()))
                .build();

    }
}
