package com.jimmy_d.notesserver.unit;

import com.jimmy_d.notesserver.database.entity.Role;
import com.jimmy_d.notesserver.database.entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void equalsTests() {
        User user1 = User.builder().id(1L).build();
        User user2 = User.builder().id(1L).build();
        User user3 = User.builder().id(2L).build();
        User userNull1 = User.builder().build();
        User userNull2 = User.builder().build();

        assertAll("equals tests",
                () -> assertEquals(user1, user1, "same instance should be equal"),
                () -> assertNotEquals(null, user1, "should not equal null"),
                () -> assertNotEquals(new Object(), user1, "different class should not be equal"),
                () -> assertNotEquals(user2, user3, "different ids should not be equal"),
                () -> assertEquals(user1, user2, "same ids should be equal"),
                () -> assertNotEquals(userNull1, userNull2, "both ids null should not be equal")
        );
    }

    @Test
    void hashCodeTests() {
        User userWithId = User.builder().id(1L).build();
        User userWithoutId = User.builder().build();
        int initialHash = userWithId.hashCode();

        assertAll("hashCode tests",
                () -> assertEquals(User.class.hashCode(), userWithoutId.hashCode(), "hashCode without id"),
                () -> assertEquals(initialHash, userWithId.hashCode(), "hashCode consistent for same instance")
        );
    }

    @Test
    void addRoleTests() {
        User user = User.builder().build();

        assertAll("addRole tests",
                () -> assertTrue(user.getRoles().isEmpty(), "roles should be empty initially"),
                () -> {
                    user.addRole(Role.ADMIN);
                    assertTrue(user.getRoles().contains(Role.ADMIN), "should contain ADMIN role after add");
                },
                () -> {
                    user.addRole(Role.ADMIN);
                    assertEquals(1, user.getRoles().size(), "adding duplicate role should not increase size");
                }
        );
    }
}