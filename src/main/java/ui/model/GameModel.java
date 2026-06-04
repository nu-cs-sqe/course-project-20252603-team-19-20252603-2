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

	public void endTurnByDrawing() {
		engine.endTurnByDrawing();
		localPlayerId = engine.getCurrentPlayerId();
	}

	public void playSkip() {
		engine.playSkip();
		localPlayerId = engine.getCurrentPlayerId();
	}

	public void playReverse() {
		engine.playReverse();
		localPlayerId = engine.getCurrentPlayerId();
	}

	public void playAttack() {
		engine.playAttack();
		localPlayerId = engine.getCurrentPlayerId();
	}

	public void playShuffle() {
		engine.playShuffle();
		localPlayerId = engine.getCurrentPlayerId();
	}

	public int getForcedTurns() {
		return engine.getForcedTurns();
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

	public void resetPlayerId() {
		localPlayerId = 0;
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
