package ui.model;

public final class PlayerDisplayInfo {
	private final String name;
	private final int handSize;
	private final boolean currentTurn;

	public PlayerDisplayInfo(String name, int handSize, boolean currentTurn) {
		this.name = name;
		this.handSize = handSize;
		this.currentTurn = currentTurn;
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
}
