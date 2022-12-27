package com.github.szsascha.game.multiplayer.client.worker;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import lombok.extern.java.Log;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

@Log
public class PingWorker implements Worker {

    private final WebSocket webSocket;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Getter
    private final WorkerType workerType = WorkerType.PING;

    private UUID sessionId;

    private long millisSent = 0l;

    @Getter
    private long currentLatency = -1l;

    private boolean end = false;

    @Getter
    private boolean connected = false;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonSerialize
    private static class Message {
        private String operation;
        private UUID sessionId;
    }

    public PingWorker(String address, int port, UUID sessionId) {
        this.sessionId = sessionId;
        webSocket = HttpClient
                .newHttpClient()
                .newWebSocketBuilder()
                .buildAsync(URI.create(String.format("ws://%s:%d/ping", address, port)), this)
                .join();
    }

    @Override
    public void run() {
        do {
            try {
                millisSent = System.currentTimeMillis();
                webSocket.sendText(objectMapper.writeValueAsString(new Message("ping", sessionId)), true);
            } catch (IOException e) {
                log.warning("Couldn't send ping");
            }

            // Ping each second
            try {
                Thread.sleep(1000l);
            } catch (InterruptedException e) {
                log.warning("Couldn't wait in ping loop");
            }
        } while(!end);
    }

    @Override
    public void onOpen(WebSocket webSocket) {
        Worker.super.onOpen(webSocket);
        connected = true;
    }

    @Override
    public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
        connected = false;
        return Worker.super.onClose(webSocket, statusCode, reason);
    }

    @Override
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
        try {
            final Message message = objectMapper.readValue(data.toString(), new TypeReference<Message>() {});
            if (message.getOperation().equals("pong")) {
                currentLatency = System.currentTimeMillis() - millisSent;
                log.info(String.format("Current latency: %d ms", currentLatency));
            } else if (message.getOperation().equals("ping")) {
                webSocket.sendText(objectMapper.writeValueAsString(new Message("pong", sessionId)), false);
            }
        } catch (RuntimeException | IOException e) {
            log.warning("Couldn't parse json: " + e.getMessage());
        }

        return Worker.super.onText(webSocket, data, last);
    }
}
