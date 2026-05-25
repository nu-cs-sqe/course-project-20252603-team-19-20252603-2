package ui.model;

import java.util.ArrayList;
import java.util.List;

public class GameSetupModel {
	private int numberPlayer;
	private List<String> playerNames;

	private static final int initialPlayerCount = 4;

	public GameSetupModel() {
		this.numberPlayer = initialPlayerCount;
		this.playerNames = new ArrayList<>();
	}

	public void setNumberPlayer(int playerCount) {
		this.numberPlayer = playerCount;
	}
	
	public int getNumberPlayer() {
		return this.numberPlayer;
	}

	public List<String> getPlayerNames() {
		return new ArrayList<>(this.playerNames);
	}

	public void capturePlayerNamesFromInputs(List<String> rawInputs, String defaultNamePrefix) {
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
}
