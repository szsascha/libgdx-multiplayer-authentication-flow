package com.github.szsascha.game.multiplayer.state.play;

import com.github.szsascha.game.multiplayer.client.GameClient;
import com.github.szsascha.game.multiplayer.state.GameState;
import com.github.szsascha.game.multiplayer.ui.UI;
import com.github.szsascha.game.multiplayer.ui.game.Chat;
import com.github.szsascha.game.multiplayer.ui.game.Ping;

import java.util.List;

public class Play implements GameState {

    private final List<UI> ui = List.of(new Ping(), new Chat());

    @Override
    public void renderGameScreen(float delta) {

    }

    @Override
    public void resizeGameScreen(int width, int height) {

    }

    @Override
    public void disposeGameScreen() {
        GameClient.getInstance().shutdownWorkers();
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
