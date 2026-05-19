package com.jimmy_d.notes_backend.http.controller.rest;

import com.jimmy_d.notes_backend.database.entity.Role;
import com.jimmy_d.notes_backend.dto.UserInfoDto;
import com.jimmy_d.notes_backend.dto.UserDetailsDto;
import com.jimmy_d.notes_backend.service.UserService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/users")
@Validated
@RequiredArgsConstructor
@PreAuthorize("hasAuthority(T(com.jimmy_d.notes_backend.database.entity.Role).ADMIN)")
public class AdminUserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public UserDetailsDto getById(@PathVariable @Positive Long id) {
        return userService.getById(id);
    }

    @GetMapping("/username/{username}")
    @ResponseStatus(HttpStatus.OK)
    public UserInfoDto getUserByUsername(@PathVariable String username) {
        return userService.getByUsername(username);
    }

    @DeleteMapping("/username/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByUsername(@PathVariable String username) {
        userService.deleteByUsername(username);
    }

    @DeleteMapping("/id/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteById(id);
    }


    @PatchMapping("/roles/add")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addUserRole(@RequestBody UserDetailsDto user, @RequestParam Role role) {
        userService.addUserRole(user, role);
    }

    @PatchMapping("/roles/remove")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUserRole(@RequestBody UserDetailsDto user, @RequestParam Role role) {
        userService.removeUserRole(user, role);
    }

}
