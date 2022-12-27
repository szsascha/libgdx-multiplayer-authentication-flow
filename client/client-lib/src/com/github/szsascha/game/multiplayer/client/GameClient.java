package com.github.szsascha.game.multiplayer.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.szsascha.game.multiplayer.client.dto.ChatMessageDto;
import com.github.szsascha.game.multiplayer.client.dto.PlayerDto;
import com.github.szsascha.game.multiplayer.client.worker.ChatWorker;
import com.github.szsascha.game.multiplayer.client.worker.PingWorker;
import com.github.szsascha.game.multiplayer.client.worker.Worker;
import com.github.szsascha.game.multiplayer.client.worker.WorkerType;
import lombok.Getter;
import lombok.extern.java.Log;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

@Log
public class GameClient {

    @Getter
    private static final GameClient instance = new GameClient();

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final HttpClient httpClient = HttpClient.newHttpClient();

    private final List<Worker> worker = new ArrayList<>();
    private final List<Thread> workerThreads = new ArrayList<>();

    private UUID sessionId = null;

    private String host = null;

    private int port = 0;

    public void register(final String host, final int port, final String username, final String password) {
        if (!GameClientState.getCurrentState().equals(GameClientState.NO_SESSION)) {
            final String errorMessage = "Invalid state to register!";
            log.severe(errorMessage);
            throw new IllegalStateException(errorMessage);
        }

        final String passwordHash = hashPassword(password);
        if (passwordHash == null) {
            log.severe("Password hash cannot be null!");
            return;
        }

        try {
            final HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(String.format("http://%s:%d/player", host, port)))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            objectMapper.writeValueAsString(new PlayerDto(username, passwordHash)))
                    )
                    .build();

            final HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 201) {
                throw new GameClientException("Couldn't register!");
            }
        } catch (ConnectException e) {
            final String errorMessage = String.format("Couldn't connect to %s:%d!", host, port);
            log.severe(errorMessage);
            throw new GameClientException(errorMessage);
        } catch (IOException | URISyntaxException | InterruptedException e) {
            log.severe(String.format("Exception during transportation: '%s'", e.getMessage()));
            throw new GameClientException(e.getMessage());
        }
    }

    public void login(final String host, final int port, final String username, final String password) {
        if (!GameClientState.getCurrentState().equals(GameClientState.NO_SESSION)) {
            final String errorMessage = "Invalid state to login!";
            log.severe(errorMessage);
            throw new IllegalStateException(errorMessage);
        }

        final String passwordHash = hashPassword(password);
        if (passwordHash == null) {
            log.severe("Password hash cannot be null!");
            return;
        }

        try {
            final HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(String.format("http://%s:%d/player/session", host, port)))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            objectMapper.writeValueAsString(new PlayerDto(username, passwordHash)))
                    )
                    .build();

            final HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 201) {
                throw new GameClientException("Couldn't login!");
            }

            this.sessionId = UUID.fromString(response.body());
            this.host = host;
            this.port = port;
            GameClientState.INITIALIZATION.switchToState();
        } catch (ConnectException e) {
            final String errorMessage = String.format("Couldn't connect to %s:%d!", host, port);
            log.severe(errorMessage);
            throw new GameClientException(errorMessage);
        } catch (IOException | URISyntaxException | InterruptedException e) {
            log.severe(String.format("Exception during transportation: '%s'", e.getMessage()));
            throw new GameClientException(e.getMessage());
        }

    }

    public void registerChatMessageConsumer(Consumer<ChatMessageDto> consumer) {
        if (findWorker(WorkerType.CHAT) instanceof ChatWorker chatWorker) {
            chatWorker.setChatMessageConsumer(consumer);
        }
    }

    public void sendChatMessage(String message) {
        if (findWorker(WorkerType.CHAT) instanceof ChatWorker chatWorker) {
            chatWorker.sendMessage(message);
        }
    }

    public void initializeGameServerConnectionAsync() {
        launchWorker(new PingWorker(host, port, sessionId));
        launchWorker(new ChatWorker(host, port, sessionId));
    }

    private void launchWorker(Worker worker) {
        final var thread = new Thread(worker);
        this.workerThreads.add(thread);
        thread.start();
        this.worker.add(worker);
    }

    public void shutdownWorkers() {
        workerThreads.forEach(Thread::stop);
    }

    private Worker findWorker(WorkerType type) {
        return worker.stream()
                .filter(worker -> worker.getWorkerType().equals(type))
                .findAny()
                .orElse(null);
    }

    public boolean isGameServerConnectionInitialized() {
        final boolean result = worker.stream().allMatch(Worker::isConnected);
        if (result) {
            GameClientState.SESSION.switchToState();
        }
        return result;
    }

    public long getCurrentLatency() {
        if (GameClientState.getCurrentState().equals(GameClientState.NO_SESSION)) {
            final String message = "Cannot get latency because no session exists.";
            log.warning(message);
            throw new GameClientException(message);
        }
        return ((PingWorker) findWorker(WorkerType.PING)).getCurrentLatency();
    }

    private String hashPassword(String password) {
        try {
            final MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(password.getBytes());
            return new String(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            log.severe(e.getMessage());
        }

        return null;
    }

}
