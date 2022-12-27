package com.github.szsascha.game.multiplayer.server.chat;

import com.github.szsascha.game.multiplayer.server.websocket.BaseMessage;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Data
public class ChatMessage extends BaseMessage {

    private LocalDateTime timestamp = LocalDateTime.now();

    private String message;

    private String username;

    public ChatMessage(UUID sessionId, String message, String username) {
        super("chatmessage", sessionId);
        this.message = message;
        this.username = username;
    }
}
