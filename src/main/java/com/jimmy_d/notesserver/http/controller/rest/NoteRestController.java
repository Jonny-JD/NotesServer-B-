package com.jimmy_d.notesserver.http.controller.rest;

import com.jimmy_d.notesserver.dto.NoteCreateDto;
import com.jimmy_d.notesserver.dto.NoteReadDto;
import com.jimmy_d.notesserver.dto.UserReadDto;
import com.jimmy_d.notesserver.exceptions.rest.NoteNotFoundException;
import com.jimmy_d.notesserver.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/v1/notes")
@RequiredArgsConstructor
public class NoteRestController {

    private final NoteService noteService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public NoteReadDto save(@RequestBody NoteCreateDto note) {
        return noteService.save(note);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public NoteReadDto getById(@PathVariable Long id) {
        return noteService.findById(id)
                .orElseThrow(() -> new NoteNotFoundException("id", id));

    }

    @GetMapping("/author")
    @ResponseStatus(HttpStatus.OK)
    public List<NoteReadDto> getAllByAuthor(@RequestBody UserReadDto author) {
        return noteService.findAllByAuthor(author);
    }

    @GetMapping("/tag")
    @ResponseStatus(HttpStatus.OK)
    public List<NoteReadDto> getAllByTag(@RequestBody String tag) {
        return noteService.findAllByTag(tag);
    }

    @DeleteMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!noteService.deleteById(id)) {
            throw new NoteNotFoundException("id", id);
        }
    }

    @GetMapping("/api/notes")
    public List<NoteReadDto> getNextNotes(@RequestParam Instant cursor) {
        return noteService.getNextNotes(cursor);
    }

}
