package com.jimmy_d.notesserver.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jimmy_d.notesserver.database.entity.Role;
import com.jimmy_d.notesserver.dto.NoteReadDto;
import com.jimmy_d.notesserver.dto.UserCreateDto;
import com.jimmy_d.notesserver.dto.UserReadDto;
import com.jimmy_d.notesserver.mapper.UserReadMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
public class RestTestUtils {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final TestFactory testFactory;
    private final UserReadMapper userReadMapper;

    public NoteReadDto createNote() throws Exception {
        var user = testFactory.createAndSaveUser();
        var dto = testFactory.dummyNoteCreateDto(userReadMapper.map(user));

        var response = mockMvc.perform(post("/api/v1/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn();

        return objectMapper.readValue(response.getResponse().getContentAsString(), NoteReadDto.class);
    }

    public UserReadDto createRestUser(String username, String email) throws Exception {
        var dto = new UserCreateDto(username, "pass", email,
                Set.of(Arrays.stream(Role.values()).map(Role::name).toArray(String[]::new)));

        var response = mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn();

        return objectMapper.readValue(response.getResponse().getContentAsString(), UserReadDto.class);
    }
}
