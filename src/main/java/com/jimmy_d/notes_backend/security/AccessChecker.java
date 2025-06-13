package com.jimmy_d.notes_backend.security;

import com.jimmy_d.notes_backend.service.NoteService;
import com.jimmy_d.notes_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("accessChecker")
@RequiredArgsConstructor
public class AccessChecker {
    private final UserService userService;
    private final NoteService noteService;

    public boolean isAccountOwner(Long userId) {
        return userService.findById(userId).map(user -> user.username()
                .equals(getCurrentUserUsername()))
                .orElse(false);
    }

    public boolean isAccountOwner(String username) {
        return userService.findByUsername(username).map(user -> user.username()
                        .equals(getCurrentUserUsername()))
                .orElse(false);
    }

    public boolean isNoteOwner(UUID noteId) {
        return noteService.findById(noteId).map(note -> note.author().username()
                .equals(getCurrentUserUsername()))
                .orElse(false);
    }

    private String getCurrentUserUsername() {
        return ((CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal())
                .getUsername();
    }
}
