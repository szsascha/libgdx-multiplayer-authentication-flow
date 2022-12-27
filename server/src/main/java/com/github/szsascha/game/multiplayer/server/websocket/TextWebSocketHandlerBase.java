package com.github.szsascha.game.multiplayer.server.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.github.szsascha.game.multiplayer.server.cache.session.CachedSession;
import com.github.szsascha.game.multiplayer.server.cache.session.SessionCache;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Log
public abstract class TextWebSocketHandlerBase<T extends BaseMessage> extends TextWebSocketHandler {

    @Getter(AccessLevel.PROTECTED)
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Getter(AccessLevel.PROTECTED)
    private final Map<UUID, WebSocketSession> webSocketSessions = new HashMap<>();

    @Autowired
    @Getter(AccessLevel.PROTECTED)
    private SessionCache sessionCache;

    public TextWebSocketHandlerBase() {
        objectMapper.registerModule(new JSR310Module());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        super.handleTextMessage(session, textMessage);
        final T message = deserializeTextMessage(textMessage);
        final UUID sessionId = message.getSessionId();
        sessionCache.get(sessionId)
                .ifPresentOrElse(
                        cachedSession -> {
                            if (!webSocketSessions.containsKey(sessionId)) {
                                webSocketSessions.put(sessionId, session);
                            }
                            handleMessage(cachedSession, message);
                        },
                        () -> handleInvalidSession(sessionId)
                );
    }

    protected T deserializeTextMessage(TextMessage textMessage) throws JsonProcessingException {
        return getObjectMapper().readValue(textMessage.getPayload(), new TypeReference<>() {});
    }

    protected void send(CachedSession session, T message) {
        try {
            webSocketSessions
                    .get(session.sessionId())
                    .sendMessage(new TextMessage(getObjectMapper().writeValueAsString(message)));
        } catch (IOException e) {
            log.warning(String.format("Error sending response. '%s'", e.getMessage()));
        }
    }

    protected void broadcast(T message) {
        getWebSocketSessions()
                .forEach(
                        (id, session) -> sessionCache.get(id).ifPresent(s -> send(s, message))
                );
    }

    protected void handleInvalidSession(UUID sessionId) {
        final String message = String.format("No session with id '%s' found!", sessionId);
        log.warning(message);

        if (webSocketSessions.containsKey(sessionId)) {
            try (final WebSocketSession session = webSocketSessions.get(sessionId)) {
                webSocketSessions.remove(sessionId);
            } catch (IOException ignored) {}
        }

        throw new IllegalStateException(message);
    }

    protected abstract void handleMessage(CachedSession session, T message);

    protected <T> T deserialize(TextMessage textMessage) throws JsonProcessingException {
        return getObjectMapper().readValue(textMessage.getPayload(), new TypeReference<>() {});
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);

    }
}
