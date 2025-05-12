package com.jimmy_d.notesserver.integration.service;

import com.jimmy_d.notesserver.database.entity.User;
import com.jimmy_d.notesserver.dto.UserCreateDto;
import com.jimmy_d.notesserver.dto.UserReadDto;
import com.jimmy_d.notesserver.integration.IntegrationTestBase;
import com.jimmy_d.notesserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class UserServiceTest extends IntegrationTestBase {

    private final User DUMMY_USER = User.builder()
            .username("DummyUsername")
            .password("DummyPassword")
            .email("DummyEmail")
            .build();

    private final UserCreateDto DUMMY_USER_CREATE_DTO = new UserCreateDto(DUMMY_USER.getUsername(),
            DUMMY_USER.getPassword(),
            DUMMY_USER.getEmail());

    private final UserService userService;


    @Test
    void save() {
        var savedUser = userService.save(DUMMY_USER_CREATE_DTO);
        assertNotNull(savedUser);
        assertNotNull(savedUser.id());
        assertEquals(savedUser.username(), DUMMY_USER_CREATE_DTO.username());
        assertEquals(savedUser.email(), DUMMY_USER_CREATE_DTO.email());
        //TODO createdBy etc.
    }

    @Test
    void findByUsername() {
        var savedUser = userService.save(DUMMY_USER_CREATE_DTO);
        var foundedUser = userService.findByUsername(DUMMY_USER_CREATE_DTO.username());
        var notFoundedUser = userService.findByUsername("");
        assertTrue(foundedUser.isPresent());
        assertFalse(notFoundedUser.isPresent());
        assertEquals(savedUser, foundedUser.get());
    }

    @Test
    void deleteUserByUsername() {
        userService.save(DUMMY_USER_CREATE_DTO);
        var positiveResult = userService.deleteByUsername(DUMMY_USER_CREATE_DTO.username());
        var negativeResult = userService.deleteByUsername(DUMMY_USER_CREATE_DTO.username());

        assertTrue(positiveResult);
        assertFalse(negativeResult);
    }

    @Test
    void deleteUserById() {
        var savedUser = userService.save(DUMMY_USER_CREATE_DTO);
        var positiveResult = userService.deleteById(savedUser.id());
        var negativeResult = userService.deleteById(savedUser.id());

        assertTrue(positiveResult);
        assertFalse(negativeResult);
    }

}