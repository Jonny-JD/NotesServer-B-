package com.jimmy_d.notesserver.service;

import com.jimmy_d.notesserver.database.entity.User;
import com.jimmy_d.notesserver.database.repository.NoteRepository;
import com.jimmy_d.notesserver.dto.NoteCreateDto;
import com.jimmy_d.notesserver.dto.NoteReadDto;
import com.jimmy_d.notesserver.mapper.NoteCreateMapper;
import com.jimmy_d.notesserver.mapper.NoteReadMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class NoteService {
    private final NoteRepository noteRepository;
    private final NoteCreateMapper noteCreateMapper;
    private final NoteReadMapper noteReadMapper;

    @Transactional
    public NoteReadDto save(NoteCreateDto noteCreateDto) {
        var note = noteCreateMapper.map(noteCreateDto);
        var savedNote = noteRepository.saveAndFlush(note);
        return noteReadMapper.map(savedNote);
    }

    public Optional<NoteReadDto> findById(Long id) {
        return noteRepository.findById(id).map(noteReadMapper::map);
        //TODO Custom exception
    }

    public List<NoteReadDto> findAllByAuthor(User author) {
        return noteRepository
                .findAllByAuthor(author)
                .stream()
                .map(noteReadMapper::map)
                .collect(Collectors.toList());
    }

    public List<NoteReadDto> findAllByTag(String tag) {
        return noteRepository
                .findAllByTag(tag)
                .stream()
                .map(noteReadMapper::map)
                .collect(Collectors.toList());
    }



}
