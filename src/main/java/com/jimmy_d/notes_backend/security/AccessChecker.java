package com.jimmy_d.notes_backend.security;

import com.jimmy_d.notes_backend.service.NoteService;
import com.jimmy_d.notes_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("accessChecker")
@RequiredArgsConstructor
public class AccessChecker {
    private final UserService userService;
    private final NoteService noteService;

    public boolean isAccountOwner(Long userId) {
        var user = userService.findById(userId);
        return user.username().equals(getCurrentUserUsername());
    }

    public boolean isAccountOwner(String username) {
        var user = userService.findByUsername(username);
        return user.username().equals(getCurrentUserUsername());
    }

    public boolean isNoteOwner(UUID noteId) {
        var note = noteService.findById(noteId);
        return note.author().username().equals(getCurrentUserUsername());
    }

    private String getCurrentUserUsername() {
        return ((CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal())
                .getUsername();
    }
}
