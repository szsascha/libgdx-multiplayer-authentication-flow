package com.github.szsascha.game.multiplayer.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;

import java.util.Set;

public interface UI {

    Skin DEFAULT_SKIN = new Skin(Gdx.files.internal("uiskin.json"), new TextureAtlas(Gdx.files.internal("uiskin.atlas")));

    Stage getStage();

    Set<Disposable> getDisposables();

    default Stage createNewStage() {
        return new Stage();
    }

    default Skin getSkin() {
        return DEFAULT_SKIN;
    }

    default void show() {
        Gdx.input.setInputProcessor(getStage());
    }

    default void render() {
        getStage().act(Gdx.graphics.getDeltaTime());
        getStage().draw();
    }

    default void dispose() {
        getStage().dispose();
        getDisposables().forEach(Disposable::dispose);
    }

    default void resize(int width, int height) {
        getStage().getViewport().update(width, height, true);
    }
}
