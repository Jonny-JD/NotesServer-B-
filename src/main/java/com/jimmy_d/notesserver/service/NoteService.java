package com.jimmy_d.notesserver.service;

import com.jimmy_d.notesserver.database.repository.NoteRepository;
import com.jimmy_d.notesserver.dto.NoteCreateDto;
import com.jimmy_d.notesserver.dto.NoteFilter;
import com.jimmy_d.notesserver.dto.NoteReadDto;
import com.jimmy_d.notesserver.mapper.NoteCreateMapper;
import com.jimmy_d.notesserver.mapper.NoteReadMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class NoteService {
    private final NoteRepository noteRepository;
    private final NoteCreateMapper noteCreateMapper;
    private final NoteReadMapper noteReadMapper;

    @Transactional
    public NoteReadDto save(NoteCreateDto dto) {
        log.info("Saving new note by authorId: {}", dto.author().id());
        var savedNote = noteRepository.save(noteCreateMapper.map(dto));
        log.info("Note saved with id: {}", savedNote.getId());
        return noteReadMapper.map(savedNote);
    }

    public Optional<NoteReadDto> findById(Long id) {
        log.debug("Finding note by id: {}", id);
        return noteRepository.findById(id).map(noteReadMapper::map);
    }

    public List<NoteReadDto> findAllByAuthorId(Long authorId) {
        log.debug("Finding all notes by authorId: {}", authorId);
        return noteRepository.findAllByAuthorId(authorId)
                .stream()
                .map(noteReadMapper::map)
                .collect(Collectors.toList());
    }

    public List<NoteReadDto> findAllByTag(String tag) {
        log.debug("Finding all notes by tag: {}", tag);
        return noteRepository.findAllByTag(tag)
                .stream()
                .map(noteReadMapper::map)
                .collect(Collectors.toList());
    }

    public List<NoteReadDto> findAllByFilter(NoteFilter filter) {
        log.debug("Finding notes by filter: {}", filter);
        return noteRepository.findAllByFilter(filter)
                .stream()
                .map(noteReadMapper::map)
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean deleteById(Long id) {
        log.info("Deleting note by id: {}", id);
        return noteRepository.findById(id)
                .map(note -> {
                    noteRepository.delete(note);
                    log.info("Deleted note with id: {}", id);
                    return true;
                }).orElseGet(() -> {
                    log.warn("Note to delete not found by id: {}", id);
                    return false;
                });
    }

    public List<NoteReadDto> getNextNotes(Instant cursor) {
        log.debug("Getting next notes after timestamp: {}", cursor);
        Pageable pageable = PageRequest.of(0, 10);
        var notes = noteRepository.findNextNotes(cursor, pageable);
        return notes.stream()
                .map(noteReadMapper::map)
                .toList();
    }

    @Transactional
    public boolean deleteAllByTag(String tag) {
        log.info("Deleting all notes by tag: {}", tag);
        return noteRepository.findFirstByTag(tag)
                .map(note -> {
                    noteRepository.deleteAllByTag(tag);
                    log.info("Deleted all notes with tag: {}", tag);
                    return true;
                }).orElseGet(() -> {
                    log.warn("No notes found with tag: {}", tag);
                    return false;
                });
    }

    @Transactional
    public boolean deleteAllByAuthor(Long authorId) {
        log.info("Deleting all notes by authorId: {}", authorId);
        return noteRepository.findFirstByAuthor_Id(authorId)
                .map(note -> {
                    noteRepository.deleteAllByAuthor_Id(authorId);
                    log.info("Deleted all notes for authorId: {}", authorId);
                    return true;
                }).orElseGet(() -> {
                    log.warn("No notes found for authorId: {}", authorId);
                    return false;
                });
    }
}
