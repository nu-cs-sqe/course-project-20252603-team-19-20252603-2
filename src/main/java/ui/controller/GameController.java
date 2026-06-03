package ui.controller;

import domain.Card;
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
				view.updatePlayerCards(model.getLocalHand());
				view.showOpponents(model.getOpponents());
				view.updateDeckCount(model.getDeckSize());
				view.updatePlayerTurn(
						appModel.getResourceBundle(),
						model.getLocalPlayerName()
				);
				view.updateHandCount(
						model.getLocalHandSize(),
						model.getLocalPlayerName()
				);
				view.clearLog();
				view.clearDiscardCard();
			}
		};
		this.startGameAction = () -> {
			model.startGame(appModel.getPlayerNames());
			refreshAction.run();
		};

		view.setOnQuitAction(router::showStart);
		view.setOnDrawAction(() -> {
			if (!model.isGameStarted() || model.ableToDrawCard()) {
				return;
			}

			Card drawn = model.drawCard();
			view.updateCardCount(model.getDeckSize());

			ResourceBundle bundle = appModel.getResourceBundle();
			String playerName = model.getLocalPlayerName();
			String message = bundle.getString("gameView.drawAction");
			view.addLog(playerName + " " + message);

			view.addPlayerCard(drawn);

			model.finishTurn();
			view.showOpponents(model.getOpponents());
			view.updatePlayerCards(model.getLocalHand());
			view.updateHandCount(
					model.getLocalHandSize(),
					model.getLocalPlayerName()
			);
			view.updatePlayerTurn(
					appModel.getResourceBundle(),
					model.getLocalPlayerName()
			);
		});
		view.setOnPlayButtonAction((handCard) -> {
			if (!model.isGameStarted()) {
				return;
			}

			handCard.setOnMouseEntered(null);
			handCard.setOnMouseExited(null);
			handCard.setOnMouseClicked(null);

			handCard.getStyleClass().remove("hand-card");
			handCard.getStyleClass().remove("hand-card-selected");
			handCard.getStyleClass().add("discard-card");

			view.addCardToDiscardPile(handCard);

			model.removeCard(handCard.getCardType());

			String playerName = model.getLocalPlayerName();
			String action = appModel.getResourceBundle().getString(
					"gameView.playAction"
			);
			String cardName = handCard.getCardName();
			String log = playerName + " " + action + " " + cardName;

			view.addLog(log);
			view.removeCardFromHand();
			view.updateCardCount(model.getDeckSize());
			view.updateHandCount(
					model.getLocalHandSize(),
					model.getLocalPlayerName()
			);
		});
	}

	public void startGame() {
		startGameAction.run();
	}

	public void refreshView() {
		refreshAction.run();
	}
}
