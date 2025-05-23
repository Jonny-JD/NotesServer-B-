package com.jimmy_d.notesserver.http.controller.rest;

import com.jimmy_d.notesserver.dto.UserCreateDto;
import com.jimmy_d.notesserver.dto.UserReadDto;
import com.jimmy_d.notesserver.exceptions.rest.UserNotExistsException;
import com.jimmy_d.notesserver.exceptions.rest.UserNotFoundException;
import com.jimmy_d.notesserver.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserReadDto create(@RequestBody @Validated UserCreateDto user) {
        log.info("Create user request: username={}", user.username());
        UserReadDto createdUser = userService.createUser(user).orElseThrow();
        log.info("User created with id={}", createdUser.id());
        return createdUser;
    }

    @DeleteMapping("/by-username/{username}")
    @PreAuthorize("hasAuthority(T(com.jimmy_d.notesserver.database.entity.Role).ADMIN)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByUsername(@PathVariable String username) {
        log.info("Delete user by username request: {}", username);
        if (!userService.deleteByUsername(username)) {
            log.warn("User not found to delete by username: {}", username);
            throw new UserNotFoundException("username", username);
        }
        log.info("User deleted by username: {}", username);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.jimmy_d.notesserver.database.entity.Role).ADMIN) or @accessChecker.isAccountOwner(#id)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable long id) {
        log.info("Delete user by id request: {}", id);
        if (!userService.deleteById(id)) {
            log.warn("User not found to delete by id: {}", id);
            throw new UserNotFoundException("id", id);
        }
        log.info("User deleted by id: {}", id);
    }

    @GetMapping("/by-username/{username}")
    public UserReadDto getByUsername(@PathVariable String username) {
        log.info("Get user by username request: {}", username);
        return userService.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("User not found by username: {}", username);
                    return new UserNotFoundException("username", username);
                });
    }

    @GetMapping("/{id}")
    public UserReadDto getById(@PathVariable Long id) {
        log.info("Get user by id request: {}", id);
        return userService.findById(id)
                .orElseThrow(() -> {
                    log.warn("User not found by id: {}", id);
                    return new UserNotFoundException("id", id);
                });
    }

    @PutMapping
    @PreAuthorize("hasAuthority(T(com.jimmy_d.notesserver.database.entity.Role).ADMIN) or @accessChecker.isAccountOwner(#user.id())")
    public UserReadDto update(@RequestBody UserReadDto user) {
        log.info("Update user request: id={}", user.id());
        UserReadDto updatedUser = userService.updateUser(user)
                .orElseThrow(() -> {
                    log.warn("User not exists for update: id={}", user.id());
                    return new UserNotExistsException("id", user.id());
                });
        log.info("User updated: id={}", updatedUser.id());
        return updatedUser;
    }
}
