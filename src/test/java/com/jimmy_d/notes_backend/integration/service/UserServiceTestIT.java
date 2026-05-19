package com.jimmy_d.notes_backend.integration.service;

import com.jimmy_d.notes_backend.database.entity.Role;
import com.jimmy_d.notes_backend.integration.IntegrationTestBase;
import com.jimmy_d.notes_backend.integration.TestFactory;
import com.jimmy_d.notes_backend.mapper.UserUpdateMapper;
import com.jimmy_d.notes_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class UserServiceTestIT extends IntegrationTestBase {

    private final UserService userService;
    private final UserUpdateMapper userUpdateMapper;
    private final TestFactory testFactory;

    @Test
    void createUserShouldSaveUserSuccessfully() {
        var userCreateDto = testFactory.dummyUserCreateDto();
        var savedUser = userService.createUser(userCreateDto);

        assertNotNull(savedUser);
        assertNotNull(savedUser.id());
        assertEquals(userCreateDto.username(), savedUser.username());
        assertEquals(userCreateDto.email(), savedUser.email());
    }

//    @Test
//    void findByUsernameShouldReturnUserIfExists() {
//        var savedUser = userService.createUser(testFactory.dummyUserCreateDto());
//
//        var foundUser = userService.findByUsername(savedUser.username());
//        var notFoundUser = userService.findByUsername("non_existent");
//
//        assertNotNull(foundUser);
//        assertNull(notFoundUser);
//        assertEquals(savedUser, foundUser);
//    }

//    @Test
//    void findByIdShouldReturnUserIfExists() {
//        var savedUser = userService.createUser(testFactory.dummyUserCreateDto());
//
//        var foundUser = userService.findById(savedUser.id());
//        var notFoundUser = userService.findById(-1L);
//
//        assertNotNull(foundUser);
//        assertNull(notFoundUser);
//        assertEquals(savedUser, foundUser);
//    }


    @Test
    void updateUserShouldAddRoleSuccessfully() {
        var savedUser = userService.createUser(testFactory.dummyUserCreateDto());
        var userToUpdate = userUpdateMapper.map(savedUser);
        userToUpdate.addRole(Role.ADMIN);

        var updatedUser = userService.updateUser(userUpdateMapper.map(userToUpdate));

        assertTrue(updatedUser.roles().contains(Role.ADMIN.name()));
        assertFalse(savedUser.roles().contains(Role.ADMIN.name()));
    }
}
