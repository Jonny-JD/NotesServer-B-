package com.jimmy_d.notes_backend.integration.proxy;

import com.jimmy_d.notes_backend.database.entity.Note;
import com.jimmy_d.notes_backend.database.entity.User;
import com.jimmy_d.notes_backend.integration.IntegrationTestBase;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.proxy.HibernateProxy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
public class NoteProxyIT extends IntegrationTestBase {

    private final EntityManager em;

    @Test
    void hashCodeAndEqualsShouldWorkWithHibernateProxy() {
        User user = new User();
        user.setUsername("author");
        user.setPassword("pass");
        user.setEmail("author@example.com");
        em.persist(user);

        Note note = new Note();
        note.setTitle("Test Note");
        note.setContent("Some content");
        note.setAuthor(user);
        note.setIsPrivate(false);
        em.persist(note);

        em.flush();
        em.clear();

        Note proxyNote = em.getReference(Note.class, note.getId());

        assertInstanceOf(HibernateProxy.class, proxyNote);

        assertEquals(proxyNote, proxyNote);
        assertEquals(proxyNote, note);
        assertEquals(note, proxyNote);

        int expectedHash = Note.class.hashCode();
        int proxyHash = proxyNote.hashCode();
        assertEquals(expectedHash, proxyHash);

        int realHash = note.hashCode();
        assertEquals(expectedHash, realHash);
    }

    @Test
    void equalsShouldReturnFalseWhenEffectiveClassesAreDifferent() {
        User user = new User();
        user.setUsername("author2");
        user.setPassword("pass2");
        user.setEmail("author2@example.com");
        em.persist(user);

        Note note = new Note();
        note.setTitle("Another Note");
        note.setContent("More content");
        note.setAuthor(user);
        note.setIsPrivate(false);
        em.persist(note);

        em.flush();
        em.clear();

        Note proxyNote = em.getReference(Note.class, note.getId());
        assertInstanceOf(HibernateProxy.class, proxyNote);

        User differentClassProxy = em.getReference(User.class, user.getId());
        assertInstanceOf(HibernateProxy.class, differentClassProxy);

        assertNotEquals(proxyNote, differentClassProxy);
        assertNotEquals(differentClassProxy, proxyNote);
    }
}
