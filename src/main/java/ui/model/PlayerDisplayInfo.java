package ui.model;

public final class PlayerDisplayInfo {
	private final String name;
	private final int handSize;
	private final boolean currentTurn;
	private boolean alive;

	public PlayerDisplayInfo(String name, int handSize, boolean currentTurn) {
		this.name = name;
		this.handSize = handSize;
		this.currentTurn = currentTurn;
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

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean living) {
		alive = living;
	}
}
