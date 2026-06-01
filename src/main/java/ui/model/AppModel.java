package ui.model;

import java.util.Locale;

public class AppModel {
	private static final Locale ENGLISH = Locale.ENGLISH;
	private static final Locale CHINESE = Locale.SIMPLIFIED_CHINESE;

	private Locale selectedLocale = ENGLISH;

	public void toggleLanguage() {
		if (selectedLocale.equals(ENGLISH)) {
			selectedLocale = CHINESE;
		} else {
			selectedLocale = ENGLISH;
		}
	}

	public Locale getSelectedLocale() {
		return selectedLocale;
	}
}
