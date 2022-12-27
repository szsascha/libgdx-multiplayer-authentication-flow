package com.github.szsascha.game.multiplayer.client;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public enum GameClientState {
    NO_SESSION,
    INITIALIZATION,
    SESSION;

    private List<Consumer<GameClientState>> onSwitchEvents = new ArrayList<>();

    @Getter
    private static GameClientState currentState = NO_SESSION;

    public void addOnSwitchEvent(Consumer<GameClientState> c) {
        onSwitchEvents.add(c);
    }

    public void switchToState() {
        currentState = this;
        onSwitchEvents.forEach(e -> e.accept(this));
    }
}
