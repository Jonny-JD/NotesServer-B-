package com.jimmy_d.notes_backend.mapper;

import com.jimmy_d.notes_backend.database.entity.Role;
import com.jimmy_d.notes_backend.database.entity.User;
import com.jimmy_d.notes_backend.dto.UserCreateDto;
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
        Optional.ofNullable(userCreateDto.rawPassword())
                .ifPresent(password -> user.setPassword(passwordEncoder.encode(password)));
        if (!user.getRoles().isEmpty()) {
            user.setRoles(userCreateDto.roles()
                    .stream()
                    .map(Role::valueOf)
                    .collect(Collectors.toSet()));
        } else {
            user.addRole(Role.USER);
        }

    }
}
