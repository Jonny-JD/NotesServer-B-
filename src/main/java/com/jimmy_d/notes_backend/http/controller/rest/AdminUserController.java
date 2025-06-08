package com.jimmy_d.notes_backend.http.controller.rest;

import com.jimmy_d.notes_backend.dto.UserReadDto;
import com.jimmy_d.notes_backend.exceptions.rest.UserNotFoundException;
import com.jimmy_d.notes_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority(T(com.jimmy_d.notes_backend.database.entity.Role).ADMIN)")
public class AdminUserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public UserReadDto getById(@PathVariable Long id) {
        return userService.findById(id)
                .orElseThrow(() -> new UserNotFoundException("id", id));
    }

    @GetMapping("/by-username/{username}")
    public UserReadDto getByUsername(@PathVariable String username) {
        return userService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("username", username));
    }


    @DeleteMapping("/by-username/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByUsername(@PathVariable String username) {
        if (!userService.deleteByUsername(username)) {
            throw new UserNotFoundException("username", username);
        }
    }

}
