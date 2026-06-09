package ui.controller;

import java.util.ResourceBundle;
import ui.model.AppModel;
import ui.navigation.ScreenRouter;
import ui.view.WinnerView;

public class WinnerController {
	public final Runnable refreshView;

	public WinnerController(WinnerView view, AppModel appModel, ScreenRouter router) {
		this.refreshView = () -> {
			ResourceBundle bundle = appModel.getResourceBundle();
			view.updateDisplay(bundle);
			view.updateWinner(bundle, appModel.getWinnerPlayerName());
		};
		view.setOnPlayAgainAction(router::showGame);
		view.setOnMainMenuAction(router::showStart);
	}

	public void refreshView() {
		refreshView.run();
	}
}
