package com.github.szsascha.game.multiplayer.server.ping;

import com.github.szsascha.game.multiplayer.server.cache.session.CachedSession;
import com.github.szsascha.game.multiplayer.server.websocket.Message;
import com.github.szsascha.game.multiplayer.server.websocket.TextWebSocketHandlerBase;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

@Component
@Log
public class PingWebSocketHandler extends TextWebSocketHandlerBase {

    @Override
    protected void handleMessage(CachedSession session, Message message) {
        send(session, new Message("pong", session.sessionId()));
    }

}
