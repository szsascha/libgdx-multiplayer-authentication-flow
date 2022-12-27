package com.github.szsascha.game.multiplayer.state.login;

import com.github.szsascha.game.multiplayer.client.GameClientState;
import com.github.szsascha.game.multiplayer.state.GameState;
import com.github.szsascha.game.multiplayer.ui.UI;
import com.github.szsascha.game.multiplayer.ui.menu.LoginMenu;

import java.util.List;

public class Login implements GameState {

    private final List<UI> ui = List.of(new LoginMenu());

    @Override
    public void renderGameScreen(float delta) {

    }

    @Override
    public void resizeGameScreen(int width, int height) {

    }

    @Override
    public void disposeGameScreen() {

    }

    @Override
    public void showGameScreen() {

    }

    @Override
    public List<UI> getUI() {
        return ui;
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
