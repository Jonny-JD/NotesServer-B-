package com.jimmy_d.notesserver.database.repository;

import com.jimmy_d.notesserver.database.entity.Note;
import com.jimmy_d.notesserver.database.querydsl.QPredicates;
import com.jimmy_d.notesserver.dto.NoteFilter;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.jimmy_d.notesserver.database.entity.QNote.note;

@RequiredArgsConstructor
public class FilterNoteRepositoryImpl implements FilterNoteRepository{

    private final EntityManager entityManager;

    @Override
    public List<Note> findAllByFilter(NoteFilter filter) {
        var predicate = QPredicates.builder()
                .add(filter.title(), note.title::eq)
                .add(filter.tag(), note.tag::eq)
                .add(filter.content(), note.content::like)
                .add(filter.authorId(), note.author.id::eq)
                .buildAnd();

        return new JPAQuery<Note>(entityManager)
                .select(note)
                .from(note)
                .where(predicate)
                .fetch();
    }
}
