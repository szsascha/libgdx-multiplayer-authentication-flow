package com.github.szsascha.game.multiplayer.ui.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.github.szsascha.game.multiplayer.ui.UI;
import lombok.Getter;
import lombok.extern.java.Log;

import java.util.HashSet;
import java.util.Set;

@Log
public class Progression implements UI {

    @Getter
    private final Stage stage = createNewStage();

    @Getter
    private final Set<Disposable> disposables = new HashSet<>();

    private final Label label = new Label("", getSkin());
    private final ProgressBar progressBar;

    public Progression() {
        final Table table = new Table();
        table.align(Align.center);
        table.setFillParent(true);
        stage.addActor(table);

        final ProgressBar.ProgressBarStyle barStyle = new ProgressBar.ProgressBarStyle();
        barStyle.background = getColoredDrawable(10, 30, Color.DARK_GRAY);
        barStyle.knobBefore = getColoredDrawable(10, 30, Color.GREEN);

        progressBar = new ProgressBar(0, 100, 10, false, barStyle);
        table.add(progressBar).width(500).height(30);

        table.row().padTop(20);
        table.add(label);
    }

    public void update(int progress, String text) {
        label.setText(text);
        progressBar.setValue(progress);
    }

    private Drawable getColoredDrawable(int width, int height, Color color) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();

        final Texture texture = new Texture(pixmap);
        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(texture));

        disposables.add(texture);
        pixmap.dispose();

        return drawable;
    }

}
