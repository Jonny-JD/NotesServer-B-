package com.jimmy_d.notesserver.integration.service;

import com.jimmy_d.notesserver.database.entity.User;
import com.jimmy_d.notesserver.dto.UserCreateDto;
import com.jimmy_d.notesserver.integration.IntegrationTestBase;
import com.jimmy_d.notesserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.runners.Parameterized;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class UserServiceTest extends IntegrationTestBase {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final static User DUMMY_USER = User.builder()
            .username("DummyUsername")
            .password("DummyPassword")
            .email("DummyEmail")
            .build();

    private final static UserCreateDto DUMMY_USER_C_DTO = new UserCreateDto(DUMMY_USER.getUsername(),
            DUMMY_USER.getPassword(),
            DUMMY_USER.getEmail());


    @Test
    void save() {
        var savedUserId = userService.save(DUMMY_USER_C_DTO);
        var savedUser = userService.findByUsername(DUMMY_USER_C_DTO.username());
        assertTrue(savedUser.isPresent());
        assertEquals(savedUser.get().getId(), savedUserId);
        assertEquals(savedUser.get().getUsername(), DUMMY_USER_C_DTO.username());
        assertTrue(passwordEncoder.matches(DUMMY_USER_C_DTO.RawPassword(), savedUser.get().getPassword()));
        assertNotNull(savedUser.get().getCreatedAt());
        //TODO createdBy etc.
    }

    @Test
    void findByUsername() {
        var savedUserId = userService.save(DUMMY_USER_C_DTO);
        var user = userService.findByUsername(DUMMY_USER_C_DTO.username());
        assertTrue(user.isPresent());
        assertEquals(user.get().getId(), savedUserId);
    }
    
    @Test
    void deleteUserByUsername() {
        userService.save(DUMMY_USER_C_DTO);
        var positiveResult = userService.deleteByUsername(DUMMY_USER_C_DTO.username());
        var negativeResult = userService.deleteByUsername(DUMMY_USER_C_DTO.username());

        assertTrue(positiveResult);
        assertFalse(negativeResult);
    }

}