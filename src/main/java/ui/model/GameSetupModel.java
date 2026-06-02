package ui.model;

import java.util.ArrayList;
import java.util.List;

public class GameSetupModel {
	private int numberPlayer;
	private List<String> playerNames;

	private static final int initialPlayerCount = 4;

	private static final String TOO_FEW_PLAYERS = "gameSetupModel.tooFewPlayers";
	private static final String TOO_MANY_PLAYERS = "gameSetupModel.tooManyPlayers";

	public GameSetupModel() {
		this.numberPlayer = initialPlayerCount;
		this.playerNames = new ArrayList<>();
	}

	public void capturePlayerNamesFromInputs(List<String> rawInputs, String defaultNamePrefix) {
		if (rawInputs.size() < 2) {
			throw new IllegalArgumentException(TOO_FEW_PLAYERS);
		}
		if (rawInputs.size() > 5) {
			throw new IllegalArgumentException(TOO_MANY_PLAYERS);
		}

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
}
