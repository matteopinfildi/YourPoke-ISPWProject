package org.example.ispwproject;

import java.util.ArrayList;
import java.util.List;

public class SessionManager {

    private List<Session> activeSessions = new ArrayList<Session>();

    private static SessionManager instance = null;

    private int nextId = -1;
    private int pokeId = -1;

    protected SessionManager() {}

    public static SessionManager getSessionManager() {
        if (SessionManager.instance == null) {
            SessionManager.instance = new SessionManager();
        }
        return instance;
    }

    public Session createSession(String username) {
        nextId++;
        return new Session(nextId, username);
    }

    public Session getSessionFromId(int id){
        for (Session session : activeSessions) {
            if(session.getSessionId() == id){
                return session;
            }
        }
        return null;
    }

//    public int getCurrentId() {return nextId;}
//    public int curGuitarId() {return ++pokeId;}
    public void addSession(Session session) {activeSessions.add(session);}
    public void removeSession(int id) {activeSessions.removeIf(session -> session.getSessionId() == id);}
}