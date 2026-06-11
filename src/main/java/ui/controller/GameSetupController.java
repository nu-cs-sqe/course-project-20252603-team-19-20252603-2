package ui.controller;

import domain.model.AppModel;
import ui.navigation.ScreenRouter;
import ui.view.GameSetupView;

public class GameSetupController {

	private static final int HEIGHT_CHANGE_PER_PLAYER = 75;

	private final Runnable refreshAction;
	private final Runnable launchAction;

	public GameSetupController(
			GameSetupView view, AppModel appModel, ScreenRouter router
	) {
		this.refreshAction = () -> {
			view.updateDisplay(appModel.getResourceBundle());
			view.updatePlayerCountButtons(
					appModel.getNumberPlayer(),
					(selection) -> {
						int playerDifference =
								selection
								- appModel.getNumberPlayer();
						view.updateSetupContainerHeight(
								HEIGHT_CHANGE_PER_PLAYER
										* playerDifference
						);
						appModel.setNumberPlayer(selection);
						refreshView();
					}
			);
			view.updatePlayerNameSection(
					appModel.getNumberPlayer(),
					appModel.getResourceBundle()
			);
		};
		this.launchAction = () -> {
			String defaultPrefix = appModel.getResourceBundle().getString(
					"gameSetupView.defaultName"
			);
			appModel.capturePlayerNamesFromInputs(
					view.getRawPlayerNameInputs(),
					defaultPrefix
			);
			router.showGame();
		};

		view.setOnBackAction(router::showStart);
		view.setOnLaunchAction(launchAction);
		refreshAction.run();
	}

	public void refreshView() {
		refreshAction.run();
	}
}
