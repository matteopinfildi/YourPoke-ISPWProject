package org.example.ispwproject;

import java.util.ArrayList;
import java.util.List;

public class SessionManager {

    private List<Session> activeSessions = new ArrayList<Session>();

    private static SessionManager instance = null;

    private int nextId = -1;
    private int pokeId = -1;

    public SessionManager() {
        // costruttore vuoto
    }

    // restituisce l'istanza
    public static SessionManager getSessionManager() {
        if (SessionManager.instance == null) {
            SessionManager.instance = new SessionManager();
        }
        return instance;
    }

    // genera una nuova sessione
    public Session createSession(String username) {
        nextId++;
        return new Session(nextId, username);
    }

    // recupera la sessione relativa all'ID passato (se esiste)
    public Session getSessionFromId(int id){
        for (Session session : activeSessions) {
            if(session.getSessionId() == id){
                return session;
            }
        }
        return null;
    }

   public int getCurrentId() {return nextId;}
    public int curPokeId() {return ++pokeId;}
    public void addSession(Session session) {activeSessions.add(session);} // aggiunge una sessione all'elenco delle sessioni attive
    public void removeSession(int id) {activeSessions.removeIf(session -> session.getSessionId() == id);}
}