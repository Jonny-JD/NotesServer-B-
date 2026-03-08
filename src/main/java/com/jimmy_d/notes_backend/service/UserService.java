package com.jimmy_d.notes_backend.service;

import com.jimmy_d.notes_backend.database.entity.Role;
import com.jimmy_d.notes_backend.database.repository.UserRepository;
import com.jimmy_d.notes_backend.dto.UserCreateDto;
import com.jimmy_d.notes_backend.dto.UserReadDto;
import com.jimmy_d.notes_backend.dto.UserUpdateDto;
import com.jimmy_d.notes_backend.exceptions.rest.UserExistsException;
import com.jimmy_d.notes_backend.mapper.UserCreateMapper;
import com.jimmy_d.notes_backend.mapper.UserReadMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

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
    public Optional<UserReadDto> createUser(UserCreateDto user) {

        var foundedUser = userRepository.findOneByEmailOrUsername(user.email(), user.username());
        if (foundedUser.isPresent()) {
            var type = user.username().equals(foundedUser.get().getUsername()) ? "username" : "email";
            if (type.equals("username")) {
                throw new UserExistsException(type, user.username());
            } else {
                throw new UserExistsException(type, user.email());
            }
        }

        return Optional.of(user)
                .map(userCreateMapper::map)
                .map(userRepository::save)
                .map(userReadMapper::map);
    }

    @Transactional
    public Optional<UserReadDto> updateUser(UserUpdateDto dto) {
        return userRepository.findById(dto.id())
                .map(user -> {
                    if (dto.username() != null) {
                        user.setUsername(dto.username());
                    }
                    if (dto.email() != null) {
                        user.setEmail(dto.email());
                    }
                    if (dto.newPassword() != null) {
                        if (!passwordEncoder.matches(dto.currentPassword(), user.getPassword())) {
                            throw new IllegalArgumentException("Wrong current password");
                        }
                        user.setPassword(passwordEncoder.encode(dto.newPassword()));
                    }
                    if (dto.roles() != null) {
                        user.setRoles(dto.roles().stream()
                                .map(Role::valueOf)
                                .collect(Collectors.toSet()));
                    }
                    return userReadMapper.map(userRepository.save(user));
                });
    }

    @Transactional
    public boolean deleteByUsername(String username) {

        return userRepository.findByUsername(username)
                .map(user -> {
                    userRepository.delete(user);
                    return true;
                }).orElse(false);
    }

    @Transactional
    public boolean deleteById(Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    userRepository.delete(user);
                    return true;
                }).orElse(false);
    }

    public Optional<UserReadDto> findByUsername(String username) {
        return userRepository.findByUsername(username).map(userReadMapper::map);
    }

    public Optional<UserReadDto> findById(Long id) {
        return userRepository.findById(id).map(userReadMapper::map);
    }
}
