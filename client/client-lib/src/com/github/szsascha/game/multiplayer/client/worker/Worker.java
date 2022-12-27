package com.github.szsascha.game.multiplayer.client.worker;

import java.net.http.WebSocket;

public interface Worker extends Runnable, WebSocket.Listener {

    boolean isConnected();

    WorkerType getWorkerType();

}
