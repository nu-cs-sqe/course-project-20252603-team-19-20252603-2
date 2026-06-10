package ui.controller;

import domain.model.AppModel;
import ui.navigation.ScreenRouter;
import ui.view.StartView;

public class StartController {
	public StartController(StartView view, AppModel appModel, ScreenRouter router) {
		view.setOnStartGameAction(router::showGameSetup);
		view.setOnHowToPlayAction(router::showInstructions);
		view.setOnLanguageAction(() -> {
			appModel.toggleLanguage();
			view.updateDisplay(appModel.getResourceBundle());
		});
		view.updateDisplay(appModel.getResourceBundle());
	}
}
