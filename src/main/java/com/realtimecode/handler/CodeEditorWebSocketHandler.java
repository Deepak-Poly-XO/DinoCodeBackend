package com.realtimecode.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class CodeEditorWebSocketHandler extends TextWebSocketHandler {

    private static final ConcurrentHashMap<String, CopyOnWriteArraySet<WebSocketSession>> sessionGroups = new ConcurrentHashMap<>();

    
    private static final ConcurrentHashMap<String, String> usernameMap = new ConcurrentHashMap<>();

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        String sessionId = extractSessionId(session);
        String username = extractUsername(session);

   
        usernameMap.put(session.getId(), username);

        sessionGroups.putIfAbsent(sessionId, new CopyOnWriteArraySet<>());
        sessionGroups.get(sessionId).add(session);

        broadcast(sessionId, session, Map.of(
            "type", "JOIN",
            "user", username
        ));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String sessionId = extractSessionId(session);

        broadcast(sessionId, session, Map.of(
            "type", "CODE",
            "content", message.getPayload()
        ));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws IOException {
        String sessionId = extractSessionId(session);

    
        String username = usernameMap.getOrDefault(session.getId(), "anonymous");


        usernameMap.remove(session.getId());

        if (sessionGroups.containsKey(sessionId)) {
            sessionGroups.get(sessionId).remove(session);

            broadcast(sessionId, session, Map.of(
                "type", "LEAVE",
                "user", username
            ));

            if (sessionGroups.get(sessionId).isEmpty()) {
                sessionGroups.remove(sessionId);
            }
        }
    }

    private void broadcast(String sessionId, WebSocketSession sender, Map<String, String> data) throws IOException {
        if (!sessionGroups.containsKey(sessionId)) return;

        String json = objectMapper.writeValueAsString(data);

        for (WebSocketSession s : sessionGroups.get(sessionId)) {
            if (s.isOpen() && !s.getId().equals(sender.getId())) {
                s.sendMessage(new TextMessage(json));
            }
        }
    }

    private String extractSessionId(WebSocketSession session) {
        String uri = session.getUri().toString();
        return UriComponentsBuilder.fromUriString(uri).build().getPathSegments().get(1);
    }

    private String extractUsername(WebSocketSession session) {
        String query = session.getUri().getQuery();
        if (query != null && query.startsWith("user=")) {
            return query.substring(5);
        }
        return "anonymous";
    }
}