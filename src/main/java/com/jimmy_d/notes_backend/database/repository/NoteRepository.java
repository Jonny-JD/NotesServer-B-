package com.jimmy_d.notes_backend.database.repository;

import com.jimmy_d.notes_backend.database.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NoteRepository extends JpaRepository<Note, UUID>, FilterNoteRepository {

    List<Note> findAllByTag(String tag);

    void deleteAllByTag(String tag);

    void deleteAllByAuthor_Id(Long authorId);

    List<Note> findAllByAuthorId(Long authorId);

    Optional<Note> findFirstByTag(String tag);

    Optional<Note> findFirstByAuthor_Id(Long authorId);
}
