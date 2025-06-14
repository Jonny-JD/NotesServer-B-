package com.jimmy_d.notes_backend.integration.security.method;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jimmy_d.notes_backend.database.repository.NoteRepository;
import com.jimmy_d.notes_backend.integration.ControllerTestBase;
import com.jimmy_d.notes_backend.integration.RestTestUtils;
import com.jimmy_d.notes_backend.integration.TestFactory;
import com.jimmy_d.notes_backend.mapper.UserReadMapper;
import com.jimmy_d.notes_backend.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
public class MethodSecurityTest extends ControllerTestBase {
    private final MockMvc mockMvc;
    private final TestFactory testFactory;
    private final NoteRepository noteRepository;
    private final RestTestUtils restTestUtils;
    private final UserReadMapper userReadMapper;
    private final ObjectMapper objectMapper;

    @BeforeEach
    public void cleanDatabase() {
        noteRepository.deleteAll();
    }


    @Test
    void shouldNotDeleteNoteById() throws Exception {
        var user = testFactory.saveUser(
                "Dummy_user_2",
                "dummy_2_pass",
                "dummy_2@email.com",
                Set.of("USER")
        );


        var userDetails = new CustomUserDetails(user.getId(), user.getUsername(), user.getPassword(), user.getRoles());
        var auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        var note = restTestUtils.createNote();

        mockMvc.perform(delete("/api/v1/notes/" + note.id()))
                .andExpectAll(
                        status().isForbidden(),
                        jsonPath("$.status").value(403),
                        jsonPath("$.errors.access").value("Access Denied"));

        assertThat(noteRepository.findById(note.id())).isNotEmpty();
    }

    @Test
    @WithMockUser(username = "Dummy_user_2", password = "dummy_2_pass", authorities = {"USER"})
    void shouldNotDeleteAllByTag() throws Exception {
        var note = restTestUtils.createNote();

        mockMvc.perform(delete("/api/admin/notes/all-by-tag/{tag}", note.tag()))
                .andExpectAll(
                        status().isForbidden(),
                        jsonPath("$.status").value(403),
                        jsonPath("$.errors.access").value("Access Denied"));

        assertThat(noteRepository.findAll()).size().isEqualTo(1);
    }

    @Test
    @WithMockUser(username = "Dummy_user_2", password = "dummy_2_pass", authorities = {"USER"})
    void shouldNotDeleteAllByAuthorId() throws Exception {
        var note = restTestUtils.createNote();

        mockMvc.perform(delete("/api/admin/notes/all-by-author/" + note.author().id()))
                .andExpectAll(
                        status().isForbidden(),
                        jsonPath("$.status").value(403),
                        jsonPath("$.errors.access").value("Access Denied"));

        assertThat(noteRepository.findAll()).size().isEqualTo(1);
    }

    @Test
    @WithMockUser(username = "Dummy_user_2", authorities = {"USER"})
    void shouldAllowUserToCreateNote() throws Exception {
        var user = testFactory.saveUser("Dummy_user_2", "dummy_2_pass", "dummy_2@email.com", Set.of("USER"));
        var dto = testFactory.dummyNoteCreateDto(userReadMapper.map(user));

        mockMvc.perform(post("/api/v1/notes")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.author.username").value(user.getUsername()));
    }

    @Test
    @WithMockUser(username = "Dummy_user_2", authorities = {"USER"})
    void shouldAllowUserToGetNoteById() throws Exception {
        var note = restTestUtils.createNote();

        mockMvc.perform(get("/api/v1/notes/" + note.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(note.id().toString()));
    }


    @Test
    @WithMockUser(username = "Dummy_user_2", authorities = {"USER"})
    void shouldNotDeleteUserByUsername() throws Exception {
        var user = testFactory.saveUser("dummyUser", "pass", "email@example.com", Set.of("USER"));

        mockMvc.perform(delete("/api/admin/users/by-username/" + user.getUsername()))
                .andExpectAll(
                        status().isForbidden(),
                        jsonPath("$.status").value(403),
                        jsonPath("$.errors.access").value("Access Denied"));
    }


    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void shouldAllowAdminToDeleteUserByUsername() throws Exception {
        var user = testFactory.saveUser("dummyUser", "pass", "email@example.com", Set.of("USER"));

        mockMvc.perform(delete("/api/admin/users/by-username/" + user.getUsername()))
                .andExpect(status().isNoContent());
    }


    @Test
    void shouldAllowUserToDeleteOwnAccount() throws Exception {
        var user = testFactory.saveUser("ownerUser", "pass", "email@example.com", Set.of("USER"));

        var userDetails = new CustomUserDetails(user.getId(), user.getUsername(), user.getPassword(), user.getRoles());
        var auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        mockMvc.perform(delete("/api/v1/users/" + user.getId()))
                .andExpect(status().isNoContent());
    }


    @Test
    void shouldNotAllowUserToDeleteOtherAccount() throws Exception {
        var user = testFactory.saveUser("ownerUser", "pass", "email@example.com", Set.of("USER"));

        var otherUser = testFactory.saveUser("otherUser", "pass", "email-other@example.com", Set.of("USER"));

        var userDetails = new CustomUserDetails(otherUser.getId(), otherUser.getUsername(), otherUser.getPassword(), otherUser.getRoles());
        var auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        mockMvc.perform(delete("/api/v1/users/" + user.getId()))
                .andExpectAll(
                        status().isForbidden(),
                        jsonPath("$.status").value(403),
                        jsonPath("$.errors.access").value("Access Denied"));
    }


    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void shouldAllowAdminToDeleteAnyUserById() throws Exception {
        var user = testFactory.saveUser("someUser", "pass", "email@example.com", Set.of("USER"));

        mockMvc.perform(delete("/api/v1/users/" + user.getId()))
                .andExpect(status().isNoContent());
    }


    @Test
    void shouldAllowUserToUpdateOwnProfile() throws Exception {
        var user = testFactory.saveUser("userToUpdate", "pass", "email@example.com", Set.of("USER"));

        var userDetails = new CustomUserDetails(user.getId(), user.getUsername(), user.getPassword(), user.getRoles());
        var auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        var userReadDto = userReadMapper.map(user);

        mockMvc.perform(put("/api/v1/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userReadDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()));
    }


    @Test
    void shouldNotAllowUserToUpdateOtherProfile() throws Exception {
        var user = testFactory.saveUser("userToUpdate", "pass", "email@example.com", Set.of("USER"));

        var otherUser = testFactory.saveUser("otherUser", "pass", "email-other@example.com", Set.of("USER"));

        var userDetails = new CustomUserDetails(otherUser.getId(), otherUser.getUsername(), otherUser.getPassword(), otherUser.getRoles());
        var auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        var userReadDto = userReadMapper.map(user);

        mockMvc.perform(put("/api/v1/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userReadDto)))
                .andExpectAll(
                        status().isForbidden(),
                        jsonPath("$.status").value(403),
                        jsonPath("$.errors.access").value("Access Denied"));
    }
}
