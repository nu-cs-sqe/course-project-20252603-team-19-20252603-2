package ui.controller;

import domain.Card;
import domain.CardType;
import java.util.ResourceBundle;
import ui.model.AppModel;
import ui.model.GameModel;
import ui.navigation.ScreenRouter;
import ui.view.CardView;
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
				model.resetPlayerId();
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

			if (drawn.getCardType() == CardType.EXPLODING_KITTEN) {
				model.explodeCurrentPlayer();
			} else {
				model.endTurnByDrawing();
			}

			view.addPlayerCard(drawn);

			view.showOpponents(model.getOpponents());
			view.updatePlayerCards(model.getLocalHand());
			view.updateHandCount(
					model.getLocalHandSize(),
					model.getLocalPlayerName()
			);
			view.updateDrawCount(
					appModel.getResourceBundle(),
					model.getForcedTurns()
			);
			view.updatePlayerTurn(
					appModel.getResourceBundle(),
					model.getLocalPlayerName()
			);
		});
		view.setOnPlayButtonAction((handCards) -> {
			if (!model.isGameStarted()) {
				return;
			}

			String playerName = model.getLocalPlayerName();
			String action = appModel.getResourceBundle().getString(
					"gameView.playAction"
			);
			String cardName = handCards.get(0).getCardName(
					appModel.getResourceBundle()
			);
			String log = playerName + " " + action + " " + cardName;

			view.addLog(log);

			for (CardView handCard : handCards) {
				handCard.setOnMouseEntered(null);
				handCard.setOnMouseExited(null);
				handCard.setOnMouseClicked(null);

				handCard.getStyleClass().remove("hand-card");
				handCard.getStyleClass().remove("hand-card-selected");
				handCard.getStyleClass().add("discard-card");

				view.addCardToDiscardPile(handCard);

				if (handCard.getCardType() == CardType.SKIP) {
					model.playSkip();
				}

				if (handCard.getCardType() == CardType.REVERSE) {
					model.playReverse();
				}

				if (handCard.getCardType() == CardType.ATTACK) {
					model.playAttack();
				}

				if (handCard.getCardType() == CardType.SHUFFLE) {
					model.playShuffle();
				}

				if (handCard.getCardType() == CardType.SEE_THE_FUTURE) {
					view.updateSeeTheFutureScreen(true);
					view.updateSeeTheFutureCards(
							appModel.getResourceBundle(),
							model.playSeeTheFuture()
					);
				}

				view.removeCardFromHand();
				view.updateCardCount(model.getDeckSize());
				view.updateHandCount(
						model.getLocalHandSize(),
						model.getLocalPlayerName()
				);
				view.updateDrawCount(
						appModel.getResourceBundle(),
						model.getForcedTurns()
				);
				view.updatePlayerCards(
						model.getLocalHand()
				);
				view.showOpponents(model.getOpponents());
			}
		});
		view.setOnSeeTheFutureDismissButton(() -> {
			view.updateSeeTheFutureScreen(false);
		});
	}

	public void startGame() {
		startGameAction.run();
	}

	public void refreshView() {
		refreshAction.run();
	}
}
