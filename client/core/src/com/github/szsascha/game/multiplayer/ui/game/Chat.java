package com.github.szsascha.game.multiplayer.ui.game;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.github.szsascha.game.multiplayer.client.GameClient;
import com.github.szsascha.game.multiplayer.ui.UI;
import lombok.Getter;
import lombok.extern.java.Log;

import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Log
public class Chat implements UI {

    private static final String TYPE_YOUR_CHAT_MESSAGE = "Type your chat message!";

    @Getter
    private final Stage stage = createNewStage();

    @Getter
    private final Set<Disposable> disposables = new HashSet<>();

    public Chat() {
        final Table table = new Table();
        table.align(Align.center);
        table.setFillParent(true);
        table.padLeft(10f);
        table.padBottom(10f);

        final Label chatLabel = new Label("", getSkin());
        chatLabel.setAlignment(Align.topLeft);
        final ScrollPane scrollPane = new ScrollPane(chatLabel, getSkin());
        scrollPane.setFadeScrollBars(true);
        scrollPane.getColor().a = 0.4f;
        scrollPane.scrollTo(0, 0, 0, 0);

        table.align(Align.bottomLeft);
        table.add(scrollPane).height(150f).width(400f);
        table.row();

        final TextField textField = new TextField(TYPE_YOUR_CHAT_MESSAGE, getSkin());
        textField.getColor().a = 0.4f;
        table.add(textField).width(400f);

        final FocusListener focusListener = new FocusListener() {
            @Override
            public boolean handle(Event event) {
                if (event instanceof FocusEvent focusEvent) {
                    if (focusEvent.isFocused()) {
                        scrollPane.getColor().a = 1.0f;
                        textField.getColor().a = 1.0f;
                        if (textField.getText().equals(TYPE_YOUR_CHAT_MESSAGE)) {
                            textField.setText("");
                        }
                    } else {
                        scrollPane.getColor().a = 0.4f;
                        textField.getColor().a = 0.4f;
                        if (textField.getText().equals("")) {
                            textField.setText(TYPE_YOUR_CHAT_MESSAGE);
                        }
                    }
                    getStage().setKeyboardFocus(textField);
                }
                return super.handle(event);
            }
        };

        scrollPane.addListener(focusListener);
        textField.addListener(focusListener);

        textField.setTextFieldListener((t, key) -> {
            if ((key == '\r' || key == '\n') && !t.getText().trim().isEmpty()){
                GameClient.getInstance().sendChatMessage(t.getText());
                t.setText("");
            }
        });

        GameClient.getInstance().registerChatMessageConsumer(message -> {
            chatLabel.setText(chatLabel.getText()
                        .append('[').append(message.getTimestamp().format(DateTimeFormatter.ofPattern("HH:mm"))).append(']')
                        .append(' ')
                        .append(message.getUsername())
                        .append(':')
                        .append(' ')
                        .append(message.getMessage()).append("\r\n")
                        .toStringAndClear());
            scrollPane.scrollTo(0, 0, 0, 0);
        });

        getStage().addCaptureListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!(event.getTarget() instanceof TextField) &&
                        !(event.getTarget() instanceof ScrollPane) &&
                        !(event.getTarget() instanceof Label)) {
                    getStage().unfocusAll();
                }
                return false;
            }
        });

        stage.addActor(table);
    }

}
