package com.github.szsascha.game.multiplayer.server.cache.session;

import java.util.UUID;

public record CachedSession(UUID sessionId, UUID playerId, String playername) {
}
