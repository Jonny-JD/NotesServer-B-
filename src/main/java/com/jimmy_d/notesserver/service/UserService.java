package com.jimmy_d.notesserver.service;

import com.jimmy_d.notesserver.database.entity.Role;
import com.jimmy_d.notesserver.database.repository.UserRepository;
import com.jimmy_d.notesserver.dto.UserCreateDto;
import com.jimmy_d.notesserver.dto.UserReadDto;
import com.jimmy_d.notesserver.exceptions.rest.UserExistsException;
import com.jimmy_d.notesserver.mapper.UserCreateMapper;
import com.jimmy_d.notesserver.mapper.UserReadMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Transactional
    public Optional<UserReadDto> createUser(UserCreateDto user) {
        log.info("Attempting to create user with username: {}, email: {}", user.username(), user.email());
        var foundedUser = userRepository.findOneByEmailOrUsername(user.email(), user.username());
        if (foundedUser.isPresent()) {
            var type = user.username().equals(foundedUser.get().getUsername()) ? "username" : "email";
            log.warn("User creation failed: {} {} already exists", type, (type.equals("username") ? user.username() : user.email()));
            if (type.equals("username")) {
                throw new UserExistsException(type, user.username());
            } else {
                throw new UserExistsException(type, user.email());
            }
        }
        var createdUser = Optional.of(user)
                .map(userCreateMapper::map)
                .map(userRepository::save)
                .map(userReadMapper::map);
        log.info("User created successfully: {}", user.username());
        return createdUser;
    }

    public Optional<UserReadDto> updateUser(UserReadDto user) {
        log.info("Attempting to update user with id: {}", user.id());
        return userRepository.findById(user.id())
                .map(userForUpdate -> {
                    userForUpdate.setUsername(user.username());
                    userForUpdate.setEmail(user.email());
                    userForUpdate.setRoles(user.roles().stream()
                            .map(Role::valueOf)
                            .collect(Collectors.toSet()));
                    var updatedUser = userReadMapper.map(userRepository.save(userForUpdate));
                    log.info("User updated successfully: id={}", user.id());
                    return updatedUser;
                });
    }

    @Transactional
    public boolean deleteByUsername(String username) {
        log.info("Attempting to delete user by username: {}", username);
        return userRepository.findByUsername(username)
                .map(user -> {
                    userRepository.delete(user);
                    log.info("User deleted by username: {}", username);
                    return true;
                }).orElseGet(() -> {
                    log.warn("User to delete by username not found: {}", username);
                    return false;
                });
    }

    @Transactional
    public boolean deleteById(Long id) {
        log.info("Attempting to delete user by id: {}", id);
        return userRepository.findById(id)
                .map(user -> {
                    userRepository.delete(user);
                    log.info("User deleted by id: {}", id);
                    return true;
                }).orElseGet(() -> {
                    log.warn("User to delete by id not found: {}", id);
                    return false;
                });
    }

    public Optional<UserReadDto> findByUsername(String username) {
        log.debug("Finding user by username: {}", username);
        return userRepository.findByUsername(username).map(userReadMapper::map);
    }

    public Optional<UserReadDto> findById(Long id) {
        log.debug("Finding user by id: {}", id);
        return userRepository.findById(id).map(userReadMapper::map);
    }
}
