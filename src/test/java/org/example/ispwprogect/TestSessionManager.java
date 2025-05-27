package org.example.ispwprogect;

import org.example.ispwproject.Session;
import org.example.ispwproject.SessionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// @author Matteo Pinfildi

class TestSessionManager {

    private SessionManager sessionManager;
    private Session session;

    // metodo effettuato prima di ogni test
    @BeforeEach
    void setUp() {
        // Inizializza il SessionManager
        sessionManager = SessionManager.getSessionManager();

        sessionManager = new SessionManager(); // Inizializza di nuovo un'istanza di SessionManager per ogni test
    }

    @Test
    void testCreateSession() {
        // Crea una nuova sessione con un username
        session = sessionManager.createSession("pinfoM");

        // Verifica che l'ID della sessione sia stato correttamente assegnato
        assertEquals(0, session.getSessionId(), "L'ID della sessione dovrebbe essere 0");

        // Verifica che il uid della sessione sia corretto
        assertEquals("pinfoM", session.getUserId(), "L'ID utente dovrebbe essere 'pinfoM'");
    }

    @Test
    void testGetSessionFromId() {
        // Crea e aggiungi una sessione al SessionManager
        session = sessionManager.createSession("pinfoM");
        sessionManager.addSession(session);

        // Recupera la sessione tramite l'ID
        Session retrievedSession = sessionManager.getSessionFromId(session.getSessionId());

        // Verifica che la sessione recuperata sia la stessa che è stata creata
        assertNotNull(retrievedSession, "La sessione recuperata non dovrebbe essere null");
        assertEquals(session.getSessionId(), retrievedSession.getSessionId(), "Gli ID delle sessioni devono corrispondere");
        assertEquals(session.getUserId(), retrievedSession.getUserId(), "Gli uid delle sessioni devono corrispondere");
    }

    @Test
    void testRemoveSession() {
        // Crea e aggiungi una sessione al SessionManager
        session = sessionManager.createSession("pinfoM");
        sessionManager.addSession(session);

        // Rimuovi la sessione
        sessionManager.removeSession(session.getSessionId());

        // Verifica che la sessione sia stata rimossa
        Session removedSession = sessionManager.getSessionFromId(session.getSessionId());
        assertNull(removedSession, "La sessione dovrebbe essere null dopo la rimozione");
    }

    @Test
    void testCreateMultipleSessions() {
        // Crea più sessioni
        Session session1 = sessionManager.createSession("pinfoM");
        Session session2 = sessionManager.createSession("matteoP");

        // Verifica che gli ID delle sessioni siano corretti e incrementali
        assertEquals(0, session1.getSessionId(), "Il primo ID della sessione dovrebbe essere 0");
        assertEquals(1, session2.getSessionId(), "Il secondo ID della sessione dovrebbe essere 1");
    }
}

