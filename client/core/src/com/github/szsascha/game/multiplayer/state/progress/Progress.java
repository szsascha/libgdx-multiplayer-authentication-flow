package com.github.szsascha.game.multiplayer.state.progress;

import com.github.szsascha.game.multiplayer.client.GameClient;
import com.github.szsascha.game.multiplayer.state.GameState;
import com.github.szsascha.game.multiplayer.state.GameStateHandler;
import com.github.szsascha.game.multiplayer.ui.UI;
import com.github.szsascha.game.multiplayer.ui.menu.Progression;

import java.util.List;

public class Progress implements GameState {

    private final Progression progression = new Progression();
    private final List<UI> ui = List.of(progression);

    public Progress() {
        GameClient.getInstance().initializeGameServerConnectionAsync();
    }

    @Override
    public void renderGameScreen(float delta) {
        progression.update(50, "Connect to game server...");
        if (GameClient.getInstance().isGameServerConnectionInitialized()) {
            progression.update(100, "Game server connection established!");
        }
    }

    @Override
    public void resizeGameScreen(int width, int height) {

    }

    @Override
    public void disposeGameScreen() {

    }

    @Override
    public List<UI> getUI() {
        return ui;
    }

    @Override
    public void showGameScreen() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }
}
