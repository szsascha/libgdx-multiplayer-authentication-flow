package com.github.szsascha.game.multiplayer.ui.game;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.github.szsascha.game.multiplayer.client.GameClient;
import com.github.szsascha.game.multiplayer.ui.UI;
import lombok.Getter;
import lombok.extern.java.Log;

import java.util.HashSet;
import java.util.Set;

@Log
public class Ping implements UI {

    @Getter
    private final Stage stage = createNewStage();

    @Getter
    private final Set<Disposable> disposables = new HashSet<>();

    private final String latencyPrefix = "Ping: ";
    private final String latencySuffix = " ms";
    private final Label latencyLabel = new Label(latencyPrefix + "n/a", getSkin());

    public Ping() {
        final Table table = new Table();
        table.align(Align.topRight);
        table.setFillParent(true);

        latencyLabel.setAlignment(Align.left);
        table.add(latencyLabel).width(120f);

        stage.addActor(table);
    }

    @Override
    public void render() {
        latencyLabel.setText(latencyPrefix + GameClient.getInstance().getCurrentLatency() + latencySuffix);
        UI.super.render();
    }
}
