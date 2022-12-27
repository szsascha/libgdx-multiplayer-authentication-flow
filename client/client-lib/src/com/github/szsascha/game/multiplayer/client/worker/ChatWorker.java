package com.github.szsascha.game.multiplayer.client.worker;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.github.szsascha.game.multiplayer.client.dto.ChatMessageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;

@Log
public class ChatWorker implements Worker {

    private final WebSocket webSocket;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final UUID sessionId;

    @Getter
    private boolean connected = false;

    @Getter
    private final WorkerType workerType = WorkerType.CHAT;

    @Setter
    private Consumer<ChatMessageDto> chatMessageConsumer = null;

    public ChatWorker(String address, int port, UUID id) {
        sessionId = id;
        webSocket = HttpClient
                .newHttpClient()
                .newWebSocketBuilder()
                .buildAsync(URI.create(String.format("ws://%s:%d/chat", address, port)), this)
                .join();
        objectMapper.registerModule(new JSR310Module());
    }

    @Override
    public void run() {

    }

    @Override
    public void onOpen(WebSocket webSocket) {
        connected = true;
        Worker.super.onOpen(webSocket);
    }

    @Override
    public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
        connected = false;
        return Worker.super.onClose(webSocket, statusCode, reason);
    }

    @Override
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
        try {
            final ChatMessageDto message = objectMapper.readValue(data.toString(), new TypeReference<ChatMessageDto>() {});
            if (chatMessageConsumer != null) {
                chatMessageConsumer.accept(ChatMessageDto.builder()
                        .username(message.getUsername())
                        .timestamp(message.getTimestamp())
                        .message(message.getMessage())
                        .build());
            } else {
                log.warning("Throw away chat message because no consumer is defined! " + data);
            }
        } catch (RuntimeException | IOException e) {
            log.warning("Couldn't parse json: " + e.getMessage());
        }

        return Worker.super.onText(webSocket, data, last);
    }

    public void sendMessage(String message) {
        try {
            final ChatMessageDto chatMessageDto = ChatMessageDto.builder()
                    .message(message)
                    .build();
            chatMessageDto.setSessionId(sessionId);
            chatMessageDto.setOperation("chatmessage");
            webSocket.sendText(objectMapper.writeValueAsString(chatMessageDto), true);
        } catch (IOException e) {
            log.warning("Couldn't send chat message: " + e.getMessage());
        }
    }
}
