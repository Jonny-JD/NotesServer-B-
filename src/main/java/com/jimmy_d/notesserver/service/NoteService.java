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
        var savedNote = noteRepository.save(noteCreateMapper.map(dto));
        return noteReadMapper.map(savedNote);
    }

    public Optional<NoteReadDto> findById(Long id) {
        return noteRepository.findById(id).map(noteReadMapper::map);
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

    @Transactional
    public boolean deleteById(Long id) {
        return noteRepository.findById(id)
                .map(note -> {
                    noteRepository.delete(note);
                    return true;
                }).orElse(false);
    }

    public List<NoteReadDto> getNextNotes(Instant cursor) {
        Pageable pageable = PageRequest.of(0, 10);
        var notes = noteRepository.findNextNotes(cursor, pageable);
        return notes.stream()
                .map(noteReadMapper::map)
                .toList();
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
