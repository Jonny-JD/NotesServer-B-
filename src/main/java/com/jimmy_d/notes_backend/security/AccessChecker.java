package com.jimmy_d.notes_backend.security;

import com.jimmy_d.notes_backend.service.NoteService;
import com.jimmy_d.notes_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;
@Slf4j
@Component("accessChecker")
@RequiredArgsConstructor
public class AccessChecker {
    private final UserService userService;
    private final NoteService noteService;

    public boolean isAccountOwner(Long userId) {
        var user = userService.getById(userId);
        return user.username().equals(getCurrentUserUsername());
    }

    public boolean isAccountOwner(String username) {
        var user = userService.getByUsername(username);
        return user.username().equals(getCurrentUserUsername());
    }

    public boolean isNoteOwner(UUID noteId) {
        var note = noteService.findById(noteId);
        return note.author().username().equals(getCurrentUserUsername());
    }

    private String getCurrentUserUsername() {
        var authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Not authenticated");
        }

        var principal = authentication.getPrincipal();

        if (principal instanceof CustomUserDetails userDetails) {
            return userDetails.getUsername();
        }

        throw new AccessDeniedException("Not authenticated");
    }
}
