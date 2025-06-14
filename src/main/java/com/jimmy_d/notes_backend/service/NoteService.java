package com.jimmy_d.notes_backend.service;

import com.jimmy_d.notes_backend.database.entity.Note;
import com.jimmy_d.notes_backend.database.repository.NoteRepository;
import com.jimmy_d.notes_backend.dto.*;
import com.jimmy_d.notes_backend.exceptions.rest.NoteNotFoundException;
import com.jimmy_d.notes_backend.mapper.NoteCreateMapper;
import com.jimmy_d.notes_backend.mapper.NotePreviewMapper;
import com.jimmy_d.notes_backend.mapper.NoteReadMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class NoteService {
    private final NoteRepository noteRepository;
    private final NoteCreateMapper noteCreateMapper;
    private final NoteReadMapper noteReadMapper;
    private final NotePreviewMapper notePreviewMapper;
    private final Pageable pageable = PageRequest.of(0, 10);


    @Transactional
    public NoteReadDto save(NoteCreateDto dto) {
        var savedNote = noteRepository.save(noteCreateMapper.map(dto));
        return noteReadMapper.map(savedNote);
    }

    public Optional<NoteReadDto> findById(UUID id) {
        return noteRepository.findById(id).map(noteReadMapper::map);
    }

    @Transactional
    public NoteReadDto updateNote(UUID id, NoteUpdateDto dto) {
        var note = noteRepository.findById(id).orElseThrow(() -> new NoteNotFoundException("id", id));
        note.setId(id);
        note.setTag(dto.tag());
        note.setTitle(dto.title());
        note.setContent(dto.content());

        return noteReadMapper.map(noteRepository.save(note));
    }


    public List<NoteReadDto> findAllByAuthorId(Long authorId) {

        return noteRepository.findAllByAuthorId(authorId)
                .stream()
                .map(noteReadMapper::map)
                .collect(Collectors.toList());
    }

    public List<NoteReadDto> findAllByTag(String tag) {
        return noteRepository.findAllByTag(tag)
                .stream()
                .map(noteReadMapper::map)
                .collect(Collectors.toList());
    }

    public List<NoteReadDto> findAllByFilter(NoteFilter filter) {
        return noteRepository.findAllByFilter(filter)
                .stream()
                .map(noteReadMapper::map)
                .collect(Collectors.toList());
    }

    public List<NotePreviewDto> findAllPreviewByFilter(NotePreviewFilter filter, Instant cursor) {
        return noteRepository.findAllPreviewByFilter(filter, cursor, pageable)
                .stream()
                .map(notePreviewMapper::map)
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean deleteById(UUID id) {
        return noteRepository.findById(id)
                .map(note -> {
                    noteRepository.delete(note);
                    return true;
                }).orElse(false);
    }

    public List<NotePreviewDto> getNextNotePreview(NotePreviewFilter filter, Instant cursor) {
        return noteRepository.findAllPreviewByFilter(filter, cursor, pageable)
                .stream()
                .map(notePreviewMapper::map)
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean deleteAllByTag(String tag) {
        return noteRepository.findFirstByTag(tag)
                .map(note -> {
                    noteRepository.deleteAllByTag(tag);
                    return true;
                }).orElse(false);
    }

    @Transactional
    public boolean deleteAllByAuthor(Long authorId) {
        return noteRepository.findFirstByAuthor_Id(authorId)
                .map(note -> {
                    noteRepository.deleteAllByAuthor_Id(authorId);
                    return true;
                }).orElse(false);
    }
}
