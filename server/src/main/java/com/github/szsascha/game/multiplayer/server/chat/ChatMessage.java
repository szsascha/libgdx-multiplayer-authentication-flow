package com.github.szsascha.game.multiplayer.server.chat;

import com.github.szsascha.game.multiplayer.server.websocket.Message;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Data
class ChatMessage extends Message {

    private LocalDateTime timestamp = LocalDateTime.now();

    private String message;

    private String username;

    public ChatMessage(UUID sessionId, String message, String username) {
        super("chatmessage", sessionId);
        this.message = message;
        this.username = username;
    }
}
