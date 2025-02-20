package com.realtimecode.handler;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class CodeEditorWebSocketHandler extends TextWebSocketHandler {

    // Store sessions by session ID
    private static final ConcurrentHashMap<String, CopyOnWriteArraySet<WebSocketSession>> sessionGroups = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String sessionId = extractSessionId(session);
        
        sessionGroups.putIfAbsent(sessionId, new CopyOnWriteArraySet<>());
        sessionGroups.get(sessionId).add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String sessionId = extractSessionId(session);
        
        // Broadcast only within the same session
        if (sessionGroups.containsKey(sessionId)) {
            for (WebSocketSession s : sessionGroups.get(sessionId)) {
                if (s.isOpen()) {
                    s.sendMessage(message);
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
        String sessionId = extractSessionId(session);
        
        if (sessionGroups.containsKey(sessionId)) {
            sessionGroups.get(sessionId).remove(session);
            if (sessionGroups.get(sessionId).isEmpty()) {
                sessionGroups.remove(sessionId);
            }
        }
    }

    // Extracts session ID from the WebSocket URL
    private String extractSessionId(WebSocketSession session) {
        String uri = session.getUri().toString();
        return UriComponentsBuilder.fromUriString(uri).build().getPathSegments().get(1); // Assumes URL format: /ws/{sessionId}
    }
}
