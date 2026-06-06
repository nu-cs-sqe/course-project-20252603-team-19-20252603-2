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

	public List<Card> playSeeTheFuture() {
		return engine.playSeeTheFuture();
	}

	public void playTargetedAttack(int targetId) {
		engine.playTargetedAttack(targetId);
		localPlayerId = engine.getCurrentPlayerId();
	}

	public void playFavor(int targetId, int cardIndex) {
		engine.playFavor(targetId, cardIndex);
	}

	public void playCatPair(int targetId) {
		engine.playCatPair(targetId);
	}

	public void defuseExplodingKitten(int reinsertIndex) {
		engine.defuseDrawnKitten(reinsertIndex);
		localPlayerId = engine.getCurrentPlayerId();
	}

	public boolean isGameOver() {
		return engine.isGameOver();
	}

	public int getWinnerId() {
		return engine.getWinnerId();
	}

	public String getPlayerName(int playerId) {
		return playerNames.get(playerId);
	}

	public void explodeCurrentPlayer() {
		engine.explodeCurrentPlayer();
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

	public int getLocalPlayerId() {
		return localPlayerId;
	}

	public boolean currentPlayerHasDefuse() {
		return engine.getPlayer(engine.getCurrentPlayerId())
				.hasCard(CardType.DEFUSE);
	}

	public List<Card> getSelectedHand(int playerId) {
		return engine.getPlayerHand(playerId);
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
		PlayerDisplayInfo playerInfo = new PlayerDisplayInfo(
				playerNames.get(playerId),
				player.getHandSize(),
				playerId
		);
		playerInfo.setCurrentTurn(engine.getCurrentPlayerId() == playerId);
		playerInfo.setAlive(player.isAlive());
		return playerInfo;
	}
}
