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

	public boolean ableToDrawCard() {
		return engine.isDeckEmpty();
	}

	public Card drawCard() {
		return engine.drawCardForCurrentPlayer();
	}

	public int getDeckSize() {
		return engine.getDrawPileSize();
	}

	public void removeCard(CardType cardType) {
		int index = engine.getPlayer(localPlayerId).getIndexOfCard(cardType);
		engine.getPlayer(localPlayerId).removeCardFromHand(index);
	}

	public void finishTurn() {
		engine.advanceToNextPlayer();
		localPlayerId = engine.getCurrentPlayerId();
	}

	public List<Card> getLocalHand() {
		return engine.getPlayerHand(localPlayerId);
	}

	public int getLocalHandSize() {
		return engine.getPlayerHand(localPlayerId).size();
	}

	public String getLocalPlayerName() {
		return playerNames.get(localPlayerId);
	}

	public List<PlayerDisplayInfo> getOpponents() {
		List<PlayerDisplayInfo> opponents = new ArrayList<>();
		for (int playerId = 0; playerId < engine.getNumPlayers(); playerId++) {
			opponents.add(toDisplayInfo(playerId));
		}
		return opponents;
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
