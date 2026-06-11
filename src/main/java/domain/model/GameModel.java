package domain.model;

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

	public boolean isDeckEmpty() {
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

	public void playNope() {
		engine.playNope(engine.getCurrentPlayerId());
		localPlayerId = engine.getCurrentPlayerId();
	}

	public void playCatPair(int targetId, List<CardType> selectedCards) {
		engine.playCatPair(targetId, selectedCards);
	}

	public void playCatTriple(
			int targetId, List<CardType> selectedCards, CardType desiredCard
	) {
		engine.playCatTriple(targetId, selectedCards, desiredCard);
	}

	public List<Card> playClone(int targetId, int cardIndex) {
		List<Card> cards = engine.playClone(targetId, cardIndex);
		localPlayerId = engine.getCurrentPlayerId();
		return cards;
	}

	public void playSuperSkip() {
		engine.playSuperSkip();
		localPlayerId = engine.getCurrentPlayerId();
	}

	public void playPersonalAttack3X() {
		engine.playPersonalAttack3X();
		localPlayerId = engine.getCurrentPlayerId();
	}

	public void playBury(int index) {
		engine.playBury(index);
		localPlayerId = engine.getCurrentPlayerId();
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

	public CardType getLastPlayedCard() {
		return engine.getLastPlayedCard();
	}

	public CardType peekTopCardForBury() {
		return engine.getDrawPile().get(0).getCardType();
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
