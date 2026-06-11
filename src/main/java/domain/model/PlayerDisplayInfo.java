package domain.model;

public final class PlayerDisplayInfo {
	private final String name;
	private final int handSize;
	private final int playerId;
	private boolean currentTurn;
	private boolean alive;

	public PlayerDisplayInfo(String name, int handSize, int playerId) {
		this.name = name;
		this.handSize = handSize;
		this.playerId = playerId;
		this.currentTurn = false;
		this.alive = true;
	}

	public String getName() {
		return name;
	}

	public int getHandSize() {
		return handSize;
	}

	public boolean isCurrentTurn() {
		return currentTurn;
	}

	public void setCurrentTurn(boolean turn) {
		this.currentTurn = turn;
	}

	public int getPlayerId() {
		return playerId;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean living) {
		alive = living;
	}
}
