package com.jimmy_d.notesserver.http.controller.rest;


import com.jimmy_d.notesserver.dto.UserCreateDto;
import com.jimmy_d.notesserver.dto.UserReadDto;
import com.jimmy_d.notesserver.exceptions.rest.UserNotFoundException;
import com.jimmy_d.notesserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public UserReadDto createUser(@RequestBody UserCreateDto user) {
        return userService.createUser(user).orElseThrow();//TODO
    }

    @DeleteMapping("/{username}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserByUsername(@PathVariable String username) {
        if (!userService.deleteByUsername(username)) {
            throw new UserNotFoundException("username", username);
        }
    }

    @DeleteMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable long id) {
        if (!userService.deleteById(id)) {
            throw new UserNotFoundException("id", id);
        }
    }

    @GetMapping("/{username}")
    public UserReadDto getUserByUsername(@PathVariable("username") String username) {
        return userService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("username", username));

    }

    @PutMapping("/{id}/update")
    public UserReadDto updateUser(@PathVariable Long id, @RequestBody UserReadDto userDto) {
        if (!id.equals(userDto.id())) {
            throw new IllegalArgumentException("ID in path and body must match");
        }

        return userService.updateUser(userDto)
                .orElseThrow(() -> new UserNotFoundException("id", id));
    }

}
