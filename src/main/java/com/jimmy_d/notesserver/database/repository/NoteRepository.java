package com.jimmy_d.notesserver.database.repository;

import com.jimmy_d.notesserver.database.entity.Note;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long>, FilterNoteRepository {

    List<Note> findAllByTag(String tag);

    @Query("""
            SELECT n FROM Note n
            WHERE n.createdAt  < :cursor
            ORDER BY n.createdAt DESC
            """)
    List<Note> findNextNotes(@Param("cursor") Instant cursor, Pageable pageable);

    void deleteAllByTag(String tag);

    void deleteAllByAuthor_Id(Long authorId);

    List<Note> findAllByAuthorId(Long authorId);

    Optional<Note> findFirstByTag(String tag);

    Optional<Note> findFirstByAuthor_Id(Long authorId);
}
