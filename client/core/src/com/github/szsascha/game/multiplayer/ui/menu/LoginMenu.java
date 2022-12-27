package com.github.szsascha.game.multiplayer.ui.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.github.szsascha.game.multiplayer.client.GameClient;
import com.github.szsascha.game.multiplayer.client.GameClientException;
import com.github.szsascha.game.multiplayer.ui.UI;
import com.github.szsascha.game.multiplayer.ui.util.InputListenerFactory;
import lombok.Getter;
import lombok.extern.java.Log;

import java.util.HashSet;
import java.util.Set;

@Log
public class LoginMenu implements UI {

    @Getter
    private final Stage stage = createNewStage();

    @Getter
    private final Set<Disposable> disposables = new HashSet<>();

    private Label infoLabel;

    private record Host(String address, int port) {
        static Host parseHost(String hostString) {
            final String[] splitted = hostString.split(":");
            final String address = splitted[0];
            int port = 8080; // Default port
            if (splitted.length > 1) {
                port = Integer.parseInt(splitted[1]);
            }
            return new Host(address, port);
        }
    }

    public LoginMenu() {
        final Table table = new Table();
        table.align(Align.top);
        table.padTop(250);
        table.setFillParent(true);
        stage.addActor(table);

        final TextField serverInput = addTextFieldRow(table, "Server:", "localhost:8080");
        table.row().padTop(10);

        final TextField usernameInput = addTextFieldRow(table, "Username:", "test");
        table.row().padTop(10);

        final TextField passwordInput = addTextFieldRow(table, "Password:", "asd123");
        passwordInput.setPasswordMode(true);
        passwordInput.setPasswordCharacter('*');
        table.row().padTop(20);

        final TextButton textButtonLogin = new TextButton("Login", getSkin());
        table.add(textButtonLogin).width(300).colspan(2);
        table.row().padTop(5);

        final TextButton textButtonRegister = new TextButton("Register", getSkin());
        table.add(textButtonRegister).width(300).colspan(2);
        table.row().padTop(30);

        infoLabel = createColoredLabel("EMPTY", Color.RED);
        infoLabel.setVisible(false);
        table.add(infoLabel).width(300).maxHeight(200).align(Align.top).expandY().colspan(2);

        textButtonLogin.addListener(InputListenerFactory.createClickListener((event, x, y) -> {
            final Host host = Host.parseHost(serverInput.getText());
            try {
                GameClient.getInstance().login(host.address, host.port, usernameInput.getText(), passwordInput.getText());
            } catch (GameClientException e) {
                updateInfoLabel(e.getMessage());
            }
        }));

        textButtonRegister.addListener(InputListenerFactory.createClickListener((event, x, y) -> {
            final Host host = Host.parseHost(serverInput.getText());
            try {
                GameClient.getInstance().register(host.address, host.port, usernameInput.getText(), passwordInput.getText());
                updateInfoLabel("Registration successful!");
            } catch (GameClientException e) {
                updateInfoLabel(e.getMessage());
            }
        }));

    }

    private TextField addTextFieldRow(final Table table, String labelText, String defaultValue) {
        final Label label = new Label(labelText, getSkin());
        final TextField text = new TextField(defaultValue, getSkin());

        table.add(label).width(100);
        table.add(text).width(200);

        return text;
    }

    private void updateInfoLabel(String text) {
        infoLabel.setVisible(true);
        infoLabel.setText(text);
    }

    private Label createColoredLabel(String text, Color textColor) {
        final Label infoLabel = new Label(text, getSkin());
        final Pixmap labelColor = new Pixmap((int) infoLabel.getWidth(), (int) infoLabel.getHeight(), Pixmap.Format.RGB888);
        disposables.add(labelColor);
        labelColor.setColor(Color.DARK_GRAY);
        labelColor.fill();
        Label.LabelStyle style = new Label.LabelStyle(infoLabel.getStyle());
        final Texture texture = new Texture(labelColor);
        disposables.add(texture);
        style.background = new Image(texture).getDrawable();
        style.fontColor = textColor;
        infoLabel.setStyle(style);
        infoLabel.setWrap(true);
        infoLabel.setHeight(infoLabel.getPrefHeight());
        infoLabel.setAlignment(Align.center);
        return infoLabel;
    }

}
