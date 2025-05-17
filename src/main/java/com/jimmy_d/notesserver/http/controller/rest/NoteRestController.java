package com.jimmy_d.notesserver.http.controller.rest;

import com.jimmy_d.notesserver.dto.NoteCreateDto;
import com.jimmy_d.notesserver.dto.NoteReadDto;
import com.jimmy_d.notesserver.dto.UserReadDto;
import com.jimmy_d.notesserver.exceptions.rest.NoteNotFoundException;
import com.jimmy_d.notesserver.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/v1/notes")
@RequiredArgsConstructor
public class NoteRestController {

    private final NoteService noteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NoteReadDto save(@RequestBody NoteCreateDto note) {
        return noteService.save(note);
    }

    @GetMapping("/{id}")
    public NoteReadDto getById(@PathVariable Long id) {
        return noteService.findById(id)
                .orElseThrow(() -> new NoteNotFoundException("id", id));
    }

    @GetMapping
    public List<NoteReadDto> getNotes(
            @RequestParam(required = false) String tag,
            @RequestBody(required = false) UserReadDto author,
            @RequestParam(required = false) Instant cursor
    ) {
        if (tag != null) {
            return noteService.findAllByTag(tag);
        }
        if (author != null) {
            return noteService.findAllByAuthor(author);
        }
        if (cursor != null) {
            return noteService.getNextNotes(cursor);
        }

        // Можно вернуть все или выбросить исключение
        return List.of(); // или throw new BadRequestException(...);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!noteService.deleteById(id)) {
            throw new NoteNotFoundException("id", id);
        }
    }
}
