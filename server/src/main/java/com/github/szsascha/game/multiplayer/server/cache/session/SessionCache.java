package com.github.szsascha.game.multiplayer.server.cache.session;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

@Service
public class SessionCache {

    private final List<CachedSession> cache = new ArrayList<>();
    private final List<Consumer<CachedSession>> putEvents = new ArrayList<>();
    private final List<Consumer<CachedSession>> removeEvents = new ArrayList<>();

    public void addRemoveEvent(Consumer<CachedSession> event) {
        removeEvents.add(event);
    }

    public void addPutEvent(Consumer<CachedSession> event) {
        putEvents.add(event);
    }

    public void put(UUID sessionId, UUID playerId, String playername) {
        final CachedSession session = new CachedSession(sessionId, playerId, playername);
        cache.add(session);
        putEvents.forEach(event -> event.accept(session));
    }

    public void remove(UUID sessionId) {
        get(sessionId).ifPresent(session -> {
            cache.remove(session);
            removeEvents.forEach(event -> event.accept(session));
        });
    }

    public Optional<CachedSession> get(UUID sessionId) {
        return cache.stream()
                .filter(session -> session.sessionId().equals(sessionId))
                .findAny();
    }

}
