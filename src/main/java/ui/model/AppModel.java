package ui.model;

import java.util.Locale;
import java.util.ResourceBundle;

public class AppModel {
	private static final String BUNDLE_BASE_NAME = "message";

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

	public ResourceBundle getResourceBundle() {
		return ResourceBundle.getBundle(BUNDLE_BASE_NAME, selectedLocale);
	}

	public Locale getSelectedLocale() {
		return selectedLocale;
	}
}
