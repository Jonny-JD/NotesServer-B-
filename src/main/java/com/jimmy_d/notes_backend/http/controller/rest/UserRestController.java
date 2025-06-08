package com.jimmy_d.notes_backend.http.controller.rest;

import com.jimmy_d.notes_backend.dto.UserCreateDto;
import com.jimmy_d.notes_backend.dto.UserReadDto;
import com.jimmy_d.notes_backend.exceptions.rest.UserNotExistsException;
import com.jimmy_d.notes_backend.exceptions.rest.UserNotFoundException;
import com.jimmy_d.notes_backend.service.UserService;
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


    @PutMapping
    @PreAuthorize("hasAuthority(T(com.jimmy_d.notes_backend.database.entity.Role).ADMIN) or @accessChecker.isAccountOwner(#user.id())")
    public UserReadDto update(@RequestBody UserReadDto user) {
        return userService.updateUser(user)
                .orElseThrow(() -> new UserNotExistsException("id", user.id()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.jimmy_d.notes_backend.database.entity.Role).ADMIN) or @accessChecker.isAccountOwner(#id)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        if (!userService.deleteById(id)) {
            throw new UserNotFoundException("id", id);
        }
    }
}
