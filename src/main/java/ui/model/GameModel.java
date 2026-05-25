package ui.model;

import domain.GameEngine;
import domain.Player;
import java.util.ArrayList;
import java.util.List;

public class GameModel {
	private static final int LOCAL_PLAYER_ID = 0;

	private GameEngine engine;
	private List<String> playerNames;
	private int cardCount;

	public void startGame(List<String> playerNames) {
		this.playerNames = new ArrayList<>(playerNames);
		this.engine = new GameEngine(playerNames.size());
		cardCount = engine.getDrawPileSize();
	}

	public boolean isGameStarted() {
		return engine != null;
	}

	public List<PlayerDisplayInfo> getOpponents() {
		List<PlayerDisplayInfo> opponents = new ArrayList<>();
		for (int playerId = 1; playerId < engine.getNumPlayers(); playerId++) {
			opponents.add(toDisplayInfo(playerId));
		}
		return opponents;
	}

	public int getDeckSize() {
		return cardCount;
	}

	public void discardCard() {
		if (cardCount > 0) {
			cardCount -= 1;
		}
	}

	public boolean isLocalPlayerTurn() {
		return engine.getCurrentPlayerId() == LOCAL_PLAYER_ID;
	}

	public int getLocalHandSize() {
		return engine.getPlayer(LOCAL_PLAYER_ID).getHandSize();
	}

	public String getLocalPlayerName() {
		return playerNames.get(LOCAL_PLAYER_ID);
	}

	private PlayerDisplayInfo toDisplayInfo(int playerId) {
		Player player = engine.getPlayer(playerId);
		return new PlayerDisplayInfo(
				playerNames.get(playerId),
				player.getHandSize(),
				engine.getCurrentPlayerId() == playerId
		);
	}
}
