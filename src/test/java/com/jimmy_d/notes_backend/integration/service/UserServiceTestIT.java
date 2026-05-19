package com.jimmy_d.notes_backend.integration.service;

import com.jimmy_d.notes_backend.IntegrationTestBase;
import com.jimmy_d.notes_backend.database.entity.Role;
import com.jimmy_d.notes_backend.dto.UserCreateDto;
import com.jimmy_d.notes_backend.dto.UserDetailsDto;
import com.jimmy_d.notes_backend.dto.UserInfoDto;
import com.jimmy_d.notes_backend.dto.UserUpdateDto;
import com.jimmy_d.notes_backend.exceptions.rest.RoleAlreadyExistsException;
import com.jimmy_d.notes_backend.exceptions.rest.RoleNotFoundException;
import com.jimmy_d.notes_backend.exceptions.rest.UserExistsException;
import com.jimmy_d.notes_backend.exceptions.rest.UserNotFoundException;
import com.jimmy_d.notes_backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static org.assertj.core.api.Assertions.*;

class UserServiceTestIT extends IntegrationTestBase {

    @Autowired
    private UserService userService;

    private UserInfoDto createdUser;

    @BeforeEach
    void setUp() {
        createdUser = userService.createUser(new UserCreateDto(
                "john",
                "password123",
                "john@mail.com",
                Set.of("USER")
        ));
    }

    // ─── createUser ───────────────────────────────────────────

    @Test
    void createUser_success() {
        assertThat(createdUser).isNotNull();
        assertThat(createdUser.username()).isEqualTo("john");
        assertThat(createdUser.email()).isEqualTo("john@mail.com");
    }

    @Test
    void createUser_duplicateUsername_throwsUserExistsException() {
        var dto = new UserCreateDto("john", "otherpass", "other@mail.com", Set.of("USER"));

        assertThatThrownBy(() -> userService.createUser(dto))
                .isInstanceOf(UserExistsException.class);
    }

    @Test
    void createUser_duplicateEmail_throwsUserExistsException() {
        var dto = new UserCreateDto("other_user", "otherpass", "john@mail.com", Set.of("USER"));

        assertThatThrownBy(() -> userService.createUser(dto))
                .isInstanceOf(UserExistsException.class);
    }

    // ─── updateUser ───────────────────────────────────────────

    @Test
    void updateUser_success() {
        var dto = new UserUpdateDto(createdUser.id(), "new_john", "new_john@mail.com", null, null);
        var updated = userService.updateUser(dto);

        assertThat(updated.username()).isEqualTo("new_john");
        assertThat(updated.email()).isEqualTo("new_john@mail.com");
    }

    @Test
    void updateUser_notFound_throwsUserNotFoundException() {
        var dto = new UserUpdateDto(999L, "new_john", "new_john@mail.com", null, null);

        assertThatThrownBy(() -> userService.updateUser(dto))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void updateUser_duplicateUsername_throwsUserExistsException() {
        userService.createUser(new UserCreateDto("other_user", "otherpass", "other@mail.com", Set.of("USER")));
        var dto = new UserUpdateDto(createdUser.id(), "other_user", "new@mail.com", null, null);

        assertThatThrownBy(() -> userService.updateUser(dto))
                .isInstanceOf(UserExistsException.class);
    }

    @Test
    void updateUser_duplicateEmail_throwsUserExistsException() {
        userService.createUser(new UserCreateDto("other_user", "otherpass", "other@mail.com", Set.of("USER")));
        var dto = new UserUpdateDto(createdUser.id(), "new_john", "other@mail.com", null, null);

        assertThatThrownBy(() -> userService.updateUser(dto))
                .isInstanceOf(UserExistsException.class);
    }

    @Test
    void updateUser_changePassword_success() {
        var dto = new UserUpdateDto(createdUser.id(), "john", "john@mail.com", "password123", "newpassword456");

        assertThatNoException().isThrownBy(() -> userService.updateUser(dto));
    }

    @Test
    void updateUser_wrongCurrentPassword_throwsIllegalArgumentException() {
        var dto = new UserUpdateDto(createdUser.id(), "john", "john@mail.com", "wrongpassword", "newpassword456");

        assertThatThrownBy(() -> userService.updateUser(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Wrong current password");
    }

    // ─── deleteById ───────────────────────────────────────────

    @Test
    void deleteById_success() {
        userService.deleteById(createdUser.id());
        var userId = createdUser.id();
        assertThatThrownBy(() -> userService.getById(userId))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void deleteById_notFound_throwsUserNotFoundException() {
        assertThatThrownBy(() -> userService.deleteById(999L))
                .isInstanceOf(UserNotFoundException.class);
    }

    // ─── deleteByUsername ─────────────────────────────────────

    @Test
    void deleteByUsername_success() {
        userService.deleteByUsername("john");

        assertThatThrownBy(() -> userService.getByUsername("john"))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void deleteByUsername_notFound_throwsUserNotFoundException() {
        assertThatThrownBy(() -> userService.deleteByUsername("ghost_user"))
                .isInstanceOf(UserNotFoundException.class);
    }

    // ─── getById ──────────────────────────────────────────────

    @Test
    void getById_success() {
        var found = userService.getById(createdUser.id());

        assertThat(found.username()).isEqualTo("john");
        assertThat(found.email()).isEqualTo("john@mail.com");
        assertThat(found.roles()).isNotEmpty();
    }

    @Test
    void getById_notFound_throwsUserNotFoundException() {
        assertThatThrownBy(() -> userService.getById(999L))
                .isInstanceOf(UserNotFoundException.class);
    }

    // ─── getByUsername ────────────────────────────────────────

    @Test
    void getByUsername_success() {
        var found = userService.getByUsername("john");

        assertThat(found.username()).isEqualTo("john");
        assertThat(found.email()).isEqualTo("john@mail.com");
    }

    @Test
    void getByUsername_notFound_throwsUserNotFoundException() {
        assertThatThrownBy(() -> userService.getByUsername("ghost_user"))
                .isInstanceOf(UserNotFoundException.class);
    }

    // ─── addUserRole ──────────────────────────────────────────

    @Test
    void addUserRole_success() {
        var details = userService.getById(createdUser.id());
        userService.addUserRole(details, Role.ADMIN);

        var updated = userService.getById(createdUser.id());
        assertThat(updated.roles()).contains("ADMIN");
    }

    @Test
    void addUserRole_alreadyExists_throwsRoleAlreadyExistsException() {
        var details = userService.getById(createdUser.id());

        assertThatThrownBy(() -> userService.addUserRole(details, Role.USER))
                .isInstanceOf(RoleAlreadyExistsException.class);
    }

    @Test
    void addUserRole_userNotFound_throwsUserNotFoundException() {
        var details = new UserDetailsDto(999L, "ghost", "ghost@mail.com", Set.of("USER"));

        assertThatThrownBy(() -> userService.addUserRole(details, Role.ADMIN))
                .isInstanceOf(UserNotFoundException.class);
    }

    // ─── removeUserRole ───────────────────────────────────────

    @Test
    void removeUserRole_success() {
        var details = userService.getById(createdUser.id());
        userService.removeUserRole(details, Role.USER);

        var updated = userService.getById(createdUser.id());
        assertThat(updated.roles()).isEmpty();
    }

    @Test
    void removeUserRole_notFound_throwsRoleNotFoundException() {
        var details = userService.getById(createdUser.id());

        assertThatThrownBy(() -> userService.removeUserRole(details, Role.ADMIN))
                .isInstanceOf(RoleNotFoundException.class);
    }

    @Test
    void removeUserRole_userNotFound_throwsUserNotFoundException() {
        var details = new UserDetailsDto(999L, "ghost", "ghost@mail.com", Set.of("USER"));

        assertThatThrownBy(() -> userService.removeUserRole(details, Role.ADMIN))
                .isInstanceOf(UserNotFoundException.class);
    }
}