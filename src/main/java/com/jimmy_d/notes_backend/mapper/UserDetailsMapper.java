package com.jimmy_d.notes_backend.mapper;

import com.jimmy_d.notes_backend.database.entity.Role;
import com.jimmy_d.notes_backend.database.entity.User;
import com.jimmy_d.notes_backend.dto.UserDetailsDto;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserDetailsMapper implements Mapper<User, UserDetailsDto> {

    @Override
    public UserDetailsDto map(User user) {
        return new UserDetailsDto(user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles()
                        .stream()
                        .map(Enum::name)
                        .collect(Collectors.toSet()));
    }

    public User map(UserDetailsDto userDetailsDto) {
        var user = new User();
        user.setId(userDetailsDto.id());
        user.setUsername(userDetailsDto.username());
        user.setEmail(userDetailsDto.email());
        userDetailsDto.roles().forEach(role -> user.addRole(Role.valueOf(role)));
        return user;

    }
}
