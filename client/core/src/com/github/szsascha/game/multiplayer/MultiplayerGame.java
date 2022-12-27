package com.github.szsascha.game.multiplayer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.github.szsascha.game.multiplayer.client.GameClient;
import com.github.szsascha.game.multiplayer.client.GameClientState;
import com.github.szsascha.game.multiplayer.state.GameState;
import com.github.szsascha.game.multiplayer.state.GameStateHandler;

public class MultiplayerGame extends Game {
	
	@Override
	public void create () {
		GameClientState.INITIALIZATION.addOnSwitchEvent(e -> GameStateHandler.PROGRESS.switchToState());
		GameClientState.SESSION.addOnSwitchEvent(e -> GameStateHandler.PLAY.switchToState());

		Gdx.graphics.setWindowedMode(1920, 1080);
		GameStateHandler.LOGIN.switchToState();
	}

	@Override
	public void dispose() {
		if (GameStateHandler.getCurrentState() != null) {
			GameStateHandler.getCurrentState().dispose();
		}
	}
}
