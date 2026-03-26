package com.realtimecode.controller;

import com.realtimecode.model.Session;
import com.realtimecode.service.SessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sessions")
@CrossOrigin("*")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    // Create a new session — email extracted from JWT automatically
    @PostMapping
    public ResponseEntity<Session> createSession(@RequestParam String name) {
        String email = getEmailFromToken();
        Session session = sessionService.createSession(name, email);
        return ResponseEntity.ok(session);
    }

    // Get a session by ID
    @GetMapping("/{sessionId}")
    public ResponseEntity<Session> getSession(@PathVariable String sessionId) {
        Session session = sessionService.getSession(sessionId);
        if (session == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(session);
    }

    // Get all active sessions
    @GetMapping
    public Collection<Session> getAllSessions() {
        return sessionService.getAllSessions();
    }

    // Get sessions created by the logged in user
    @GetMapping("/my")
    public List<Session> getMySessions() {
        String email = getEmailFromToken();
        return sessionService.getMysessions(email);
    }

    // Save code for a session
    @PutMapping("/{sessionId}/code")
    public ResponseEntity<Session> saveCode(
            @PathVariable String sessionId,
            @RequestBody Map<String, String> body) {

        String code = body.get("code");
        Session session = sessionService.saveCode(sessionId, code);
        if (session == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(session);
    }

    // Helper — gets email from the JWT token via Spring Security
    private String getEmailFromToken() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }
}