package com.jimmy_d.notesserver.integration.service;

import com.jimmy_d.notesserver.database.entity.Role;
import com.jimmy_d.notesserver.integration.IntegrationTestBase;
import com.jimmy_d.notesserver.integration.TestFactory;
import com.jimmy_d.notesserver.mapper.UserReadMapper;
import com.jimmy_d.notesserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class UserServiceTest extends IntegrationTestBase {

    private final UserService userService;
    private final UserReadMapper userReadMapper;
    private final TestFactory testFactory;


    @Test
    void createUser() {
        var userCreateDto = testFactory.dummyUserCreateDto();
        var savedUser = userService.createUser(userCreateDto)
                .orElseThrow(() -> new RuntimeException("Failed to create user"));

        assertNotNull(savedUser);
        assertNotNull(savedUser.id());
        assertEquals(userCreateDto.username(), savedUser.username());
        assertEquals(userCreateDto.email(), savedUser.email());
    }

    @Test
    void findByUsername() {
        var savedUser = userService.createUser(testFactory.dummyUserCreateDto())
                .orElseThrow(() -> new RuntimeException("Failed to create user"));

        var foundUser = userService.findByUsername(savedUser.username());
        var notFoundUser = userService.findByUsername("non_existent");

        assertTrue(foundUser.isPresent());
        assertFalse(notFoundUser.isPresent());
        assertEquals(savedUser, foundUser.get());
    }

    @Test
    void deleteUserByUsername() {
        var userDto = testFactory.dummyUserCreateDto();
        userService.createUser(userDto);

        var firstDelete = userService.deleteByUsername(userDto.username());
        var secondDelete = userService.deleteByUsername(userDto.username());

        assertTrue(firstDelete);
        assertFalse(secondDelete);
    }

    @Test
    void deleteUserById() {
        var savedUser = userService.createUser(testFactory.dummyUserCreateDto())
                .orElseThrow(() -> new RuntimeException("Failed to create user"));

        var firstDelete = userService.deleteById(savedUser.id());
        var secondDelete = userService.deleteById(savedUser.id());

        assertTrue(firstDelete);
        assertFalse(secondDelete);
    }

    @Test
    void updateUser() {
        var savedUser = userService.createUser(testFactory.dummyUserCreateDto())
                .orElseThrow(() -> new RuntimeException("Failed to create user"));
        var userToUpdate = userReadMapper.map(savedUser);
        userToUpdate.addRole(Role.ADMIN);

        var updatedUser = userService.updateUser(userReadMapper.map(userToUpdate))
                .orElseThrow(() -> new RuntimeException("Failed to create user"));

        assertTrue(updatedUser.roles().contains(Role.ADMIN.name()));
        assertFalse(savedUser.roles().contains(Role.ADMIN.name()));
    }
}