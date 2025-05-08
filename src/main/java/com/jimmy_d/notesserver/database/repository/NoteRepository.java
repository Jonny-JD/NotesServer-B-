package com.jimmy_d.notesserver.database.repository;

import com.jimmy_d.notesserver.database.entity.Note;
import com.jimmy_d.notesserver.database.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findAllByAuthor(User author);

    List<Note> findAllByTag(String tag);
}
