package domain.model;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class AppModel {
	private static final String BUNDLE_BASE_NAME = "message";

	private static final Locale ENGLISH = Locale.ENGLISH;
	private static final Locale CHINESE = Locale.SIMPLIFIED_CHINESE;

	private Locale selectedLocale = ENGLISH;

	private final GameSetupModel setupModel = new GameSetupModel();

	private String winnerPlayerName;

	public void toggleLanguage() {
		if (selectedLocale.equals(ENGLISH)) {
			selectedLocale = CHINESE;
		} else {
			selectedLocale = ENGLISH;
		}
	}

	public ResourceBundle getResourceBundle() {
		return ResourceBundle.getBundle(BUNDLE_BASE_NAME, selectedLocale);
	}

	public Locale getSelectedLocale() {
		return selectedLocale;
	}

	public int getNumberPlayer() {
		return setupModel.getNumberPlayer();
	}

	public void setNumberPlayer(int playerCount) {
		setupModel.setNumberPlayer(playerCount);
	}

	public List<String> getPlayerNames() {
		return setupModel.getPlayerNames();
	}

	public void capturePlayerNamesFromInputs(
			List<String> rawInputs, String defaultNamePrefix
	) {
		setupModel.capturePlayerNamesFromInputs(rawInputs, defaultNamePrefix);
	}

	public void setWinnerPlayerName(String winnerPlayerName) {
		this.winnerPlayerName = winnerPlayerName;
	}

	public String getWinnerPlayerName() {
		return winnerPlayerName;
	}
}
