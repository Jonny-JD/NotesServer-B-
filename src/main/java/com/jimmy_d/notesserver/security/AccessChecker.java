package com.jimmy_d.notesserver.security;

import com.jimmy_d.notesserver.service.NoteService;
import com.jimmy_d.notesserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

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

    public boolean isNoteOwner(Long noteId) {
        return noteService.findById(noteId).map(note -> note.author().username()
                .equals(getCurrentUserUsername()))
                .orElse(false);
    }

    private String getCurrentUserUsername() {
        return ((User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal())
                .getUsername();
    }
}
