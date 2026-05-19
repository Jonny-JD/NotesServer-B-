package com.jimmy_d.notes_backend.service;

import com.jimmy_d.notes_backend.database.entity.Role;
import com.jimmy_d.notes_backend.database.repository.UserRepository;
import com.jimmy_d.notes_backend.dto.UserCreateDto;
import com.jimmy_d.notes_backend.dto.UserReadDto;
import com.jimmy_d.notes_backend.dto.UserUpdateDto;
import com.jimmy_d.notes_backend.exceptions.rest.UserExistsException;
import com.jimmy_d.notes_backend.exceptions.rest.UserNotExistsException;
import com.jimmy_d.notes_backend.exceptions.rest.UserNotFoundException;
import com.jimmy_d.notes_backend.mapper.UserCreateMapper;
import com.jimmy_d.notes_backend.mapper.UserReadMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

import static com.jimmy_d.notes_backend.exceptions.rest.Types.*;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final UserCreateMapper userCreateMapper;
    private final UserReadMapper userReadMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserReadDto createUser(UserCreateDto dto) {
        var findUser = userRepository.findOneByEmailOrUsername(dto.email(), dto.username());

        if (findUser.isPresent()) {
            var type =
                    dto.username()
                            .equalsIgnoreCase
                                    (findUser.get().getUsername())
                            ? USERNAME : EMAIL;
            if (type.equals(USERNAME)) {
                throw new UserExistsException(type.value(), dto.username());
            } else {
                throw new UserExistsException(type.value(), dto.email());
            }
        }
        var newUser = userCreateMapper.map(dto);

        return userReadMapper.map(userRepository.save(newUser));

    }

    @Transactional
    public UserReadDto updateUser(UserUpdateDto dto) {
        var findUser = userRepository.findById(dto.id())
                .orElseThrow(() -> new UserNotExistsException(ID.value(), dto.id()));

        if (dto.username().equalsIgnoreCase(findUser.getUsername())) {
            throw new UserExistsException(USERNAME.value(), dto.username());
        }
        if (dto.email().equalsIgnoreCase(findUser.getEmail())) {
            throw new UserExistsException(EMAIL.value(), dto.email());
        }

        findUser.setUsername(dto.username());
        findUser.setEmail(dto.email());
        if (dto.newPassword() != null) {
            if (!passwordEncoder.matches(dto.currentPassword(), findUser.getPassword())) {
                throw new IllegalArgumentException("Wrong current password");
            }
            findUser.setPassword(passwordEncoder.encode(dto.newPassword()));
        }
        if (dto.roles() != null) {
            findUser.setRoles(dto.roles().stream()
                    .map(Role::valueOf)
                    .collect(Collectors.toSet()));
        }

        return userReadMapper.map(findUser);
    }


    @Transactional
    public void deleteByUsername(String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(USERNAME.value(), username));
        userRepository.delete(user);
    }

    @Transactional
    public void deleteById(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(ID.value(), id));

        userRepository.delete(user);
    }

    public UserReadDto findByUsername(String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(USERNAME.value(), username));

        return userReadMapper.map(user);
    }

    public UserReadDto findById(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(ID.value(), id));
        return userReadMapper.map(user);
    }
}
