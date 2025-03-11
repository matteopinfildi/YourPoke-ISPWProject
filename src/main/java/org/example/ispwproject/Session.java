package org.example.ispwproject;

public class Session {
    private String uid;
    private final int sessionId;

    public String getUserId() {return uid;}

    public Session(int id, String uid) {
        this.uid = uid;
        this.sessionId = id;
    }

    public int getSessionId() {return sessionId;}
}

