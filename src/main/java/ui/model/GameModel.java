package ui.model;

import domain.Card;
import domain.CardType;
import domain.GameEngine;
import domain.Player;
import java.util.ArrayList;
import java.util.List;

public class GameModel {
	private int localPlayerId = 0;

	private GameEngine engine;
	private List<String> playerNames;

	public void startGame(List<String> playerNames) {
		this.playerNames = new ArrayList<>(playerNames);
		this.engine = new GameEngine(playerNames.size());
	}

	public boolean isGameStarted() {
		return engine != null;
	}

	public String getLocalPlayerName() {
		return playerNames.get(localPlayerId);
	}
}
