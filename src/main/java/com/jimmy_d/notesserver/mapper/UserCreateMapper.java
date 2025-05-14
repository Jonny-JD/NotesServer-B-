package com.jimmy_d.notesserver.mapper;

import com.jimmy_d.notesserver.database.entity.Role;
import com.jimmy_d.notesserver.database.entity.User;
import com.jimmy_d.notesserver.dto.UserCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserCreateMapper implements Mapper<UserCreateDto, User> {

    private final PasswordEncoder passwordEncoder;

    @Override
    public User map(UserCreateDto userCreateDto) {
        User user = new User();
        copy(userCreateDto, user);
        return user;
    }

    private void copy(UserCreateDto userCreateDto, User user) {
        user.setUsername(userCreateDto.username());
        user.setEmail(userCreateDto.email());
        Optional.ofNullable(userCreateDto.RawPassword())
                .ifPresent(password -> user.setPassword(passwordEncoder.encode(password)));
        user.setRoles(userCreateDto.roles()
                .stream()
                .map(Role::valueOf)
                .collect(Collectors.toSet()));
    }
}
