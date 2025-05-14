package com.jimmy_d.notesserver.dto;

import java.util.Set;

public record UserCreateDto(String username,
                            String RawPassword,
                            String email,
                            Set<String> roles) {
}
