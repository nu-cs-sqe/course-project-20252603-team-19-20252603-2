package domain.model;

import java.util.ArrayList;
import java.util.List;

public class GameSetupModel {
	private int numberPlayer;
	private List<String> playerNames;

	private static final int initialPlayerCount = 4;
	private static final int minPlayerCount = 2;
	private static final int maxPlayerCount = 5;

	private static final String TOO_FEW_PLAYERS = "gameSetupModel.tooFewPlayers";
	private static final String TOO_MANY_PLAYERS = "gameSetupModel.tooManyPlayers";

	public GameSetupModel() {
		this.numberPlayer = initialPlayerCount;
		this.playerNames = new ArrayList<>();
	}

	private void checkPlayerNameInputs(List<String> rawInputs) {
		if (rawInputs.size() < minPlayerCount) {
			throw new IllegalArgumentException(TOO_FEW_PLAYERS);
		}
		if (rawInputs.size() > maxPlayerCount) {
			throw new IllegalArgumentException(TOO_MANY_PLAYERS);
		}
	}

	public void capturePlayerNamesFromInputs(List<String> rawInputs, String defaultNamePrefix) {
		checkPlayerNameInputs(rawInputs);
		List<String> names = new ArrayList<>();
		for (int i = 0; i < rawInputs.size(); i++) {
			String name = rawInputs.get(i).trim();
			if (name.isEmpty()) {
				name = defaultNamePrefix + " " + (i + 1);
			}
			names.add(name);
		}
		this.playerNames = names;
	}

	public List<String> getPlayerNames() {
		return new ArrayList<>(this.playerNames);
	}

	public void setNumberPlayer(int playerCount) {
		this.numberPlayer = playerCount;
	}

	public int getNumberPlayer() {
		return this.numberPlayer;
	}
}
