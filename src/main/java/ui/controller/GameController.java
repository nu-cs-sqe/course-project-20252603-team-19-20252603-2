package ui.controller;

import ui.model.AppModel;
import ui.model.GameModel;
import ui.navigation.ScreenRouter;
import ui.view.GameView;

public class GameController {
	private final GameModel model;
	private final Runnable refreshAction;
	private final Runnable startGameAction;

	public GameController(GameView view, AppModel appModel, ScreenRouter router) {
		this.model = new GameModel();
		this.refreshAction = () -> {
			view.updateDisplay(appModel.getResourceBundle());
			if (model.isGameStarted()) {
				view.showOpponents(model.getOpponents());
				view.updateDeckCount(model.getDeckSize());
				view.updateTurnIndicator(model.isLocalPlayerTurn());
				view.showLocalHand(
						model.getLocalHandSize(), model.getLocalPlayerName()
				);
			}
		};
		this.startGameAction = () -> {
			model.startGame(appModel.getPlayerNames());
			refreshAction.run();
		};

		view.setOnQuitAction(router::showStart);
	}

	public void startGame() {
		startGameAction.run();
	}

	public void refreshView() {
		refreshAction.run();
	}
}
