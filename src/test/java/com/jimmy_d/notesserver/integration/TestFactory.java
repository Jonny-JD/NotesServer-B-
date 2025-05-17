package com.jimmy_d.notesserver.integration;

import com.jimmy_d.notesserver.database.entity.Role;
import com.jimmy_d.notesserver.database.entity.User;
import com.jimmy_d.notesserver.dto.NoteCreateDto;
import com.jimmy_d.notesserver.dto.UserCreateDto;
import com.jimmy_d.notesserver.dto.UserReadDto;
import com.jimmy_d.notesserver.mapper.UserReadMapper;
import com.jimmy_d.notesserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class TestFactory {

    private final UserService userService;
    private final UserReadMapper userReadMapper;

    // Константы
    private static final String DUMMY_USERNAME = "Dummy_user_#1";
    private static final String DUMMY_PASSWORD = "dummy_#1_pass";
    private static final String DUMMY_EMAIL = "dummy_#1_email";
    private static final String DUMMY_TITLE = "dummy_title_#1_1";
    private static final String DUMMY_TAG = "dummy_tag_#1_1";
    private static final String DUMMY_CONTENT = "dummy_content_#1_1";
    private static final Set<String> DUMMY_ROLES = Set.of(Role.USER.name());

    public UserCreateDto dummyUserCreateDto() {
        return new UserCreateDto(
                DUMMY_USERNAME,
                DUMMY_PASSWORD,
                DUMMY_EMAIL,
                DUMMY_ROLES
        );
    }

    public User createAndSaveUser() {
        return userService.createUser(dummyUserCreateDto())
                .map(userReadMapper::map)
                .orElseThrow(() -> new RuntimeException("Failed to create user"));
    }

    public UserReadDto dummyUserReadDto(Long userId) {
        return new UserReadDto(
                userId,
                DUMMY_USERNAME,
                DUMMY_EMAIL,
                DUMMY_ROLES
        );
    }

    public NoteCreateDto dummyNoteCreateDto(UserReadDto author) {
        return new NoteCreateDto(
                DUMMY_TITLE,
                DUMMY_TAG,
                DUMMY_CONTENT,
                author
        );
    }

    public NoteCreateDto dummyNoteCreateDto(Long userId, String username, String email) {
        return new NoteCreateDto(
                DUMMY_TITLE,
                DUMMY_TAG,
                DUMMY_CONTENT,
                new UserReadDto(userId, username, email, DUMMY_ROLES)
        );
    }

}