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
import java.util.UUID;

import static com.jimmy_d.notes_backend.exceptions.rest.Types.ID;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class NoteService {
    private final NoteRepository noteRepository;
    private final NoteCreateMapper noteCreateMapper;
    private final NoteReadMapper noteReadMapper;
    private final NotePreviewMapper notePreviewMapper;
    private static final int PAGE_SIZE = 10;


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
        note.setTag(dto.tag().isEmpty() ? null : dto.tag());
        note.setTitle(dto.title().isEmpty() ? null : dto.title());
        note.setContent(dto.content());
        note.setIsPrivate(dto.isPrivate());

        return noteReadMapper.map(note);
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
        return noteRepository.findAllPreviewByFilter(filter, cursor, PageRequest.of(0, PAGE_SIZE))
                .stream()
                .map(notePreviewMapper::map)
                .toList();
    }

    @Transactional
    public void deleteById(UUID id) {
        if (!noteRepository.existsById(id)) {
            throw new NoteNotFoundException(ID.value(), id);
        }
        noteRepository.deleteById(id);
    }

    @Transactional
    public void deleteAllByTag(String tag) {
        noteRepository.deleteAllByTag(tag);
    }

    @Transactional
    public void deleteAllByAuthor(Long authorId) {
        noteRepository.deleteAllByAuthor_Id(authorId);
    }
}
