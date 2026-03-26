package com.realtimecode.service;

import com.realtimecode.model.Session;
import com.realtimecode.repository.SessionRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public Session createSession(String name, String createdBy) {
        Session session = new Session(name, createdBy);
        return sessionRepository.save(session);
    }

    public Session getSession(String sessionId) {
        return sessionRepository.findById(sessionId).orElse(null);
    }

    public Collection<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    public List<Session> getMysessions(String email) {
        return sessionRepository.findByCreatedBy(email);
    }

    public Session saveCode(String sessionId, String code) {
        Session session = sessionRepository.findById(sessionId).orElse(null);
        if (session == null) return null;

        session.setCode(code);
        session.setUpdatedAt(LocalDateTime.now());
        return sessionRepository.save(session);
    }
}