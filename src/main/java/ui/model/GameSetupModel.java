package ui.model;

import java.util.ArrayList;
import java.util.List;

public class GameSetupModel {
	private int numberPlayer;
	private List<String> playerNames;

	private static final int initialPlayerCount = 4;

	private static final String TOO_FEW_PLAYERS = "gameSetupModel.tooFewPlayers";

	public GameSetupModel() {
		this.numberPlayer = initialPlayerCount;
		this.playerNames = new ArrayList<>();
	}

	public void capturePlayerNamesFromInputs(List<String> rawInputs, String defaultNamePrefix) {
		throw new IllegalArgumentException(TOO_FEW_PLAYERS);
	}
}
