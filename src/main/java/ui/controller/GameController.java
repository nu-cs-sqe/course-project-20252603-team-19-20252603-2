package ui.controller;

import java.util.ResourceBundle;
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
				view.clearLog();
			}
		};
		this.startGameAction = () -> {
			model.startGame(appModel.getPlayerNames());
			refreshAction.run();
		};

		view.setOnQuitAction(router::showStart);
		view.setOnDrawAction(() -> {
			model.discardCard();
			view.updateCardCount(model.getDeckSize());
			if (model.getDeckSize() > 0) {
				ResourceBundle bundle = appModel.getResourceBundle();
				String playerName = model.getLocalPlayerName();
				String message = bundle.getString("gameView.drawAction");
				view.addLog(playerName + " " + message);
			}
		});
	}

	public void startGame() {
		startGameAction.run();
	}

	public void refreshView() {
		refreshAction.run();
	}
}
