package com.github.szsascha.game.multiplayer.state;

import com.badlogic.gdx.*;
import com.github.szsascha.game.multiplayer.state.login.Login;
import com.github.szsascha.game.multiplayer.state.play.Play;
import com.github.szsascha.game.multiplayer.state.progress.Progress;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.lang.reflect.InvocationTargetException;

@RequiredArgsConstructor
@Log
public enum GameStateHandler {

    LOGIN(Login.class),
    PROGRESS(Progress.class),
    PLAY(Play.class);

    private final Class<?> stateClass;
    @Getter
    private static GameState currentState;

    public void switchToState() {
        final String previousGameStateName;
        if (((Game) Gdx.app.getApplicationListener()).getScreen() instanceof GameState previousGameState) {
            previousGameStateName = previousGameState.getClass().getSimpleName();
        } else {
            previousGameStateName = "*INIT*";
        }
        Gdx.app.getApplicationListener().dispose();
        try {
            final GameState newState = (GameState) stateClass.getDeclaredConstructor().newInstance();
            currentState = newState;
            ((Game) Gdx.app.getApplicationListener()).setScreen(newState);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.severe(e.getMessage());
            e.printStackTrace();
        }
        log.info(String.format("Switch game state from '%s' to '%s'.", previousGameStateName, stateClass.getSimpleName()));
    }

}
