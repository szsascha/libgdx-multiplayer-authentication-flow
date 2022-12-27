package com.github.szsascha.game.multiplayer.ui.util;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.github.szsascha.game.multiplayer.util.TriConsumer;

public class InputListenerFactory {

    public static ClickListener createClickListener(TriConsumer<InputEvent, Float, Float> consumer) {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                consumer.accept(event, x, y);
            }
        };
    }

}
