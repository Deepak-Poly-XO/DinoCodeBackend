package com.realtimecode.service;

import com.realtimecode.model.Session;
import org.springframework.stereotype.Service;
import java.util.*;


@Service
public class SessionService {
  private final Map<String, Session> activeSessions = new HashMap<>();

    public Session createSession(String name) {
        Session session = new Session(name);
        activeSessions.put(session.getId(), session);
        return session;
    }

    public Session getSession(String sessionId) {
        return activeSessions.get(sessionId);
    }

    public Collection<Session> getAllSessions() {
        return activeSessions.values();
    }
}
