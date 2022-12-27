package com.github.szsascha.game.multiplayer.server.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.szsascha.game.multiplayer.server.cache.session.CachedSession;
import com.github.szsascha.game.multiplayer.server.websocket.TextWebSocketHandlerBase;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

@Component
@Log
public class ChatWebSocketHandler extends TextWebSocketHandlerBase<ChatMessage> {

    @Override
    protected void handleMessage(CachedSession session, ChatMessage message) {
        if (message.getOperation().equals("chatmessage")) {
            broadcast(new ChatMessage(session.sessionId(), message.getMessage(), session.playername()));
        }
    }

    @Override
    protected ChatMessage deserializeTextMessage(TextMessage textMessage) throws JsonProcessingException {
        return getObjectMapper().readValue(textMessage.getPayload(), new TypeReference<>() {});
    }
}
