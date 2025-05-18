package com.jimmy_d.notesserver.http.controller.rest;

import com.jimmy_d.notesserver.dto.NoteCreateDto;
import com.jimmy_d.notesserver.dto.NoteFilter;
import com.jimmy_d.notesserver.dto.NoteReadDto;
import com.jimmy_d.notesserver.exceptions.rest.InvalidNoteQueryException;
import com.jimmy_d.notesserver.exceptions.rest.NoteNotFoundException;
import com.jimmy_d.notesserver.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) Long authorId
    ) {
        if (title == null && tag == null && content == null && authorId == null) {
            throw new InvalidNoteQueryException();
        }
        return noteService.findAllByFilter(new NoteFilter(title, tag, content, authorId));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!noteService.deleteById(id)) {
            throw new NoteNotFoundException("id", id);
        }
    }
}
