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
        return userService.createUser(user).orElseThrow();
    }

    @DeleteMapping("/by-username/{username}")
    @PreAuthorize("hasAuthority(T(com.jimmy_d.notesserver.database.entity.Role).ADMIN)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByUsername(@PathVariable String username) {
        if (!userService.deleteByUsername(username)) {
            throw new UserNotFoundException("username", username);
        }
        
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.jimmy_d.notesserver.database.entity.Role).ADMIN) or @accessChecker.isAccountOwner(#id)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable long id) {
        if (!userService.deleteById(id)) {
            throw new UserNotFoundException("id", id);
        }
        
    }

    @GetMapping("/by-username/{username}")
    public UserReadDto getByUsername(@PathVariable String username) {
        return userService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("username", username));
    }

    @GetMapping("/{id}")
    public UserReadDto getById(@PathVariable Long id) {
        return userService.findById(id)
                .orElseThrow(() -> new UserNotFoundException("id", id));
    }

    @PutMapping
    @PreAuthorize("hasAuthority(T(com.jimmy_d.notesserver.database.entity.Role).ADMIN) or @accessChecker.isAccountOwner(#user.id())")
    public UserReadDto update(@RequestBody UserReadDto user) {
        return userService.updateUser(user)
                .orElseThrow(() -> new UserNotExistsException("id", user.id()));
    }
}
