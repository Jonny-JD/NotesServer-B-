package com.jimmy_d.notes_backend.service;

import com.jimmy_d.notes_backend.database.repository.NoteRepository;
import com.jimmy_d.notes_backend.dto.*;
import com.jimmy_d.notes_backend.exceptions.rest.NoteNotFoundException;
import com.jimmy_d.notes_backend.exceptions.rest.Types;
import com.jimmy_d.notes_backend.exceptions.rest.UserNotFoundException;
import com.jimmy_d.notes_backend.mapper.NoteCreateMapper;
import com.jimmy_d.notes_backend.mapper.NotePreviewMapper;
import com.jimmy_d.notes_backend.mapper.NoteReadMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.jimmy_d.notes_backend.exceptions.rest.Types.*;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class NoteService {
    private final NoteRepository noteRepository;
    private final NoteCreateMapper noteCreateMapper;
    private final NoteReadMapper noteReadMapper;
    private final NotePreviewMapper notePreviewMapper;


    @Transactional
    public NoteReadDto save(NoteCreateDto dto) {
        var savedNote = noteRepository.save(noteCreateMapper.map(dto));
        return noteReadMapper.map(savedNote);
    }

    public NoteReadDto findById(UUID id) {
        var note = noteRepository.findById(id)
                .orElseThrow(() -> new NoteNotFoundException(ID.value(), id));

        return noteReadMapper.map(note);
    }

    @Transactional
    public NoteReadDto updateNote(UUID id, NoteUpdateDto dto) {
        var note = noteRepository.findById(id).orElseThrow(() -> new NoteNotFoundException(ID.value(), id));
        note.setId(id);
        note.setTag(dto.tag().isEmpty() ? null : dto.tag());
        note.setTitle(dto.title().isEmpty() ? null : dto.title());
        note.setContent(dto.content());
        note.setIsPrivate(dto.isPrivate());

        return noteReadMapper.map(noteRepository.save(note));
    }


    public List<NoteReadDto> findAllByAuthorId(Long authorId) {

        return noteRepository.findAllByAuthorId(authorId)
                .stream()
                .map(noteReadMapper::map)
                .toList();
    }

    public List<NoteReadDto> findAllByTag(String tag) {
        return noteRepository.findAllByTag(tag)
                .stream()
                .map(noteReadMapper::map)
                .toList();
    }

    public List<NoteReadDto> findAllByFilter(NoteFilter filter) {
        return noteRepository.findAllByFilter(filter)
                .stream()
                .map(noteReadMapper::map)
                .toList();
    }

    public List<NotePreviewDto> findAllPreviewByFilter(NotePreviewFilter filter, Instant cursor) {
        return noteRepository.findAllPreviewByFilter(filter, cursor, PageRequest.of(0, 10))
                .stream()
                .map(notePreviewMapper::map)
                .toList();
    }

    @Transactional
    public void deleteById(UUID id) {
        var note = noteRepository.findById(id)
                .orElseThrow(() -> new NoteNotFoundException(ID.value(), id));

        noteRepository.delete(note);
    }


    @Transactional
    public void deleteAllByTag(String tag) {
        var notes = noteRepository.findAllByTag(tag);
        if (notes.isEmpty()) throw new NoteNotFoundException(TAG.value(), tag);

        noteRepository.deleteAll(notes);
    }

    @Transactional
    public void deleteAllByAuthor(Long authorId) {
        var notes = noteRepository.findAllByAuthorId(authorId);
        if (notes.isEmpty()) throw new UserNotFoundException(ID.value(), authorId);

        noteRepository.deleteAll(notes);
    }
}
