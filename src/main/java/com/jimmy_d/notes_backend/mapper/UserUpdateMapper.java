package com.jimmy_d.notes_backend.mapper;

import com.jimmy_d.notes_backend.database.entity.Role;
import com.jimmy_d.notes_backend.database.entity.User;
import com.jimmy_d.notes_backend.dto.UserReadDto;
import com.jimmy_d.notes_backend.dto.UserUpdateDto;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserUpdateMapper implements Mapper<User, UserUpdateDto> {

    @Override
    public UserUpdateDto map(User user) {
        return new UserUpdateDto(user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getPassword(),
                user.getRoles()
                        .stream()
                        .map(Enum::name)
                        .collect(Collectors.toSet()));
    }

    public User map(UserUpdateDto userUpdateDto) {
        return User.builder()
                .id(userUpdateDto.id())
                .username(userUpdateDto.username())
                .email(userUpdateDto.email())
                .roles(userUpdateDto.roles()
                        .stream()
                        .map(Role::valueOf)
                        .collect(Collectors.toSet()))
                .build();

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
