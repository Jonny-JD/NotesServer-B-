package com.jimmy_d.notesserver.integration.service;

import com.jimmy_d.notesserver.database.entity.Role;
import com.jimmy_d.notesserver.exceptions.rest.UserNotFoundException;
import com.jimmy_d.notesserver.integration.IntegrationTestBase;
import com.jimmy_d.notesserver.integration.TestFactory;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@RequiredArgsConstructor
class UserDetailsServiceImplIT extends IntegrationTestBase {

    private final TestFactory testFactory;
    private final UserDetailsService userDetailsService;

    @Test
    void shouldLoadUserByUsername() {
        var username = "testuser";
        testFactory.saveUser(username, "password", "test@example.com", Set.of(Role.USER.name()));

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(username);
        assertThat(userDetails.getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .containsExactly("USER");
    }

    @Test
    void shouldThrowIfUserNotFound() {
        assertThatThrownBy(() -> userDetailsService.loadUserByUsername("unknown_user"))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("unknown_user");
    }
}
