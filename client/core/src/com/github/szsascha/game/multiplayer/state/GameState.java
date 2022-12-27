package com.github.szsascha.game.multiplayer.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.github.szsascha.game.multiplayer.ui.UI;

import java.util.List;
import java.util.function.Consumer;

public interface GameState extends Screen {

    @Override
    default void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        iterateUIStages(UI::render);
        renderGameScreen(delta);
    }

    @Override
    default void resize(int width, int height) {
        iterateUIStages(ui -> ui.resize(width, height));
        resizeGameScreen(width, height);
    }

    @Override
    default void dispose() {
        iterateUIStages(UI::dispose);
        disposeGameScreen();
    }

    @Override
    default void show() {
        iterateUIStages(UI::show);
    }

    void showGameScreen();

    void renderGameScreen(float delta);

    void resizeGameScreen(int width, int height);

    void disposeGameScreen();

    default void iterateUIStages(Consumer<UI> consumer) {
        getUI().forEach(consumer);
    }

    List<UI> getUI();

}
