package ui.controller;

import domain.Card;
import domain.CardType;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import ui.model.AppModel;
import ui.model.GameModel;
import ui.model.PlayerDisplayInfo;
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
				view.updateDrawCount(
						appModel.getResourceBundle(),
						model.getForcedTurns()
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

			String log = computeLog(handCards, appModel);
			view.addLog(log);

			for (CardView handCard : handCards) {
				playCard(handCard, view, appModel);
			}
		});
		view.setOnSeeTheFutureDismissButton(() -> {
			view.updateSeeTheFutureScreen(false);
		});
	}

	public void startGame() {
		startGameAction.run();
	}

	private String computeLog(List<CardView> handCards, AppModel appModel) {
		String playerName = model.getLocalPlayerName();
		String action = appModel.getResourceBundle().getString(
				"gameView.playAction"
		);
		String cardName = handCards.get(0).getCardName(
				appModel.getResourceBundle()
		);
		String log = playerName + action + cardName;
		if (appModel.getSelectedLocale() == Locale.ENGLISH) {
			log = playerName + " " + action + " " + cardName;
		}
		return log;
	}

	private void discardCard(CardView card, GameView view) {
		card.setOnMouseEntered(null);
		card.setOnMouseExited(null);
		card.setOnMouseClicked(null);

		card.getStyleClass().remove("hand-card");
		card.getStyleClass().remove("hand-card-selected");
		card.getStyleClass().add("discard-card");

		view.addCardToDiscardPile(card);
	}

	private void refreshAfterPlay(GameView view, AppModel appModel) {
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
		view.updatePlayerTurn(
				appModel.getResourceBundle(),
				model.getLocalPlayerName()
		);
		view.showOpponents(model.getOpponents());
	}

	private boolean isLivingOpponent(PlayerDisplayInfo player) {
		return player.getPlayerId() != model.getLocalPlayerId()
				&& player.isAlive();
	}

	private void playCard(CardView card, GameView view, AppModel appModel) {
		discardCard(card, view);

		if (card.getCardType() == CardType.SKIP) {
			model.playSkip();
		}

		if (card.getCardType() == CardType.REVERSE) {
			model.playReverse();
		}

		if (card.getCardType() == CardType.ATTACK) {
			model.playAttack();
		}

		if (card.getCardType() == CardType.SHUFFLE) {
			model.playShuffle();
		}

		if (card.getCardType() == CardType.SEE_THE_FUTURE) {
			List<Card> topThreeCards = model.playSeeTheFuture();
			view.updateSeeTheFutureScreen(true);
			view.updateSeeTheFutureCards(
					appModel.getResourceBundle(),
					topThreeCards
			);
		}

		if (card.getCardType() == CardType.TARGETED_ATTACK) {
			List<PlayerDisplayInfo> players = model.getOpponents().stream()
					.filter(this::isLivingOpponent)
					.collect(Collectors.toList());

			view.updateTargetedAttackScreen(true);
			view.updateTargetedAttackPlayers(
					players,
					(playerId) -> {
						view.updateTargetedAttackScreen(false);
						model.playTargetedAttack(playerId);
						refreshAfterPlay(view, appModel);
					}
			);
		}

		refreshAfterPlay(view, appModel);
	}
}
