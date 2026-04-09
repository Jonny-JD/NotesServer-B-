package com.jimmy_d.notes_backend.service;

import com.jimmy_d.notes_backend.database.repository.NoteRepository;
import com.jimmy_d.notes_backend.dto.*;
import com.jimmy_d.notes_backend.exceptions.rest.NoteNotFoundException;
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
import java.util.Optional;
import java.util.UUID;

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

    public Optional<NoteReadDto> findById(UUID id) {
        return noteRepository.findById(id).map(noteReadMapper::map);
    }

    @Transactional
    public NoteReadDto updateNote(UUID id, NoteUpdateDto dto) {
        var note = noteRepository.findById(id).orElseThrow(() -> new NoteNotFoundException("id", id));
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
    public boolean deleteById(UUID id) {
        return noteRepository.findById(id)
                .map(note -> {
                    noteRepository.delete(note);
                    return true;
                }).orElse(false);
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
