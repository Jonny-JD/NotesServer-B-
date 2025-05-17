package com.jimmy_d.notesserver.integration.proxy;

import com.jimmy_d.notesserver.database.entity.Note;
import com.jimmy_d.notesserver.database.entity.User;
import com.jimmy_d.notesserver.integration.IntegrationTestBase;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.proxy.HibernateProxy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
public class UserProxyIT extends IntegrationTestBase {

    private final EntityManager em;

    @Test
    void hashCodeAndEqualsShouldWorkWithHibernateProxy() {

        User user = new User();
        user.setUsername("proxyUser");
        user.setPassword("password");
        user.setEmail("proxy@example.com");

        em.persist(user);
        em.flush();
        em.clear();

        User proxyUser = em.getReference(User.class, user.getId());

        assertInstanceOf(HibernateProxy.class, proxyUser);
        assertEquals(proxyUser, proxyUser);

        assertEquals(proxyUser, user);
        assertEquals(user, proxyUser);

        int expectedHash = User.class.hashCode();
        int proxyHash = proxyUser.hashCode();
        assertEquals(expectedHash, proxyHash);

        int realHash = user.hashCode();
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
        em.persist(note);

        em.flush();
        em.clear();

        User proxyUser = em.getReference(User.class, user.getId());
        assertInstanceOf(HibernateProxy.class, proxyUser);

        Note proxyNote = em.getReference(Note.class, note.getId());
        assertInstanceOf(HibernateProxy.class, proxyNote);

        assertNotEquals(proxyUser, proxyNote);
        assertNotEquals(proxyNote, proxyUser);
    }
}
