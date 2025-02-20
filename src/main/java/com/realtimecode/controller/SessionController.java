package com.realtimecode.controller;

import com.realtimecode.model.Session;
import com.realtimecode.service.SessionService;
import org.springframework.web.bind.annotation.*;
import java.util.Collection;


@RestController
@RequestMapping("/sessions")
@CrossOrigin("*")
public class SessionController {
  private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    //Create a new session
    @PostMapping
    public Session createSession(@RequestParam String name) {
        return sessionService.createSession(name);
    }

    //Get a session by ID
    @GetMapping("/{sessionId}")
    public Session getSession(@PathVariable String sessionId) {
        return sessionService.getSession(sessionId);
    }

    //Get all active sessions
    @GetMapping
    public Collection<Session> getAllSessions() {
        return sessionService.getAllSessions();
    }
}
