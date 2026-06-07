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

	private static final int CAT_PAIR_SIZE = 2;

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
				handleExplodingKitten(view, appModel);
			} else {
				view.addPlayerCard(drawn);
				model.endTurnByDrawing();
			}

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

			playCard(handCards, view, appModel);
		});
		view.setOnSeeTheFutureDismissButton(view::hideSeeTheFutureScreen);
		view.setOnDefuseButton((reinsertIndex) -> {
			model.defuseExplodingKitten(reinsertIndex);
			view.hideDefuseScreen();
			refreshAfterPlay(view, appModel);
			handleGameOver(appModel, router);
			CardView defuseCard = new CardView("Defuse");
			discardCard(defuseCard, view);
		});
		view.setOnExplodeButton(() -> {
			model.explodeCurrentPlayer();
			view.hideDefuseScreen();
			refreshAfterPlay(view, appModel);
			handleGameOver(appModel, router);
		});
	}

	private void handleGameOver(AppModel appModel, ScreenRouter router) {
		if (model.isGameOver()) {
			appModel.setWinnerPlayerName(
					model.getPlayerName(model.getWinnerId())
			);
			router.showWinner();
		}
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

	private void discardCard(List<CardView> cards, GameView view) {
		for (CardView card : cards) {
			card.setOnMouseEntered(null);
			card.setOnMouseExited(null);
			card.setOnMouseClicked(null);

			card.getStyleClass().remove("hand-card");
			card.getStyleClass().remove("hand-card-selected");
			card.getStyleClass().add("discard-card");

			view.addCardToDiscardPile(card);
		}
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
		view.updatePlayerCards(model.getLocalHand());
		view.updatePlayerTurn(
				appModel.getResourceBundle(),
				model.getLocalPlayerName()
		);
		view.showOpponents(model.getOpponents());
	}

	private List<PlayerDisplayInfo> livingOpponents() {
		return model.getOpponents().stream()
				.filter(this::isLivingOpponent)
				.collect(Collectors.toList());
	}

	private boolean isLivingOpponent(PlayerDisplayInfo player) {
		return player.getPlayerId() != model.getLocalPlayerId()
				&& player.isAlive();
	}

	private void playCard(List<CardView> cards, GameView view, AppModel appModel) {
		CardView card = cards.get(0);

		if (cards.size() == CAT_PAIR_SIZE) {
			view.showCatCardScreen();
			view.updateCatCardsPlayer(
					livingOpponents(),
					(targetId) -> {
						discardCard(cards, view);
						view.hideCatCardScreen();
						model.playCatPair(targetId);
						refreshAfterPlay(view, appModel);
					}
			);
		}

		if (card.getCardType() == CardType.SKIP) {
			discardCard(cards, view);
			model.playSkip();
		}

		if (card.getCardType() == CardType.REVERSE) {
			discardCard(cards, view);
			model.playReverse();
		}

		if (card.getCardType() == CardType.ATTACK) {
			discardCard(cards, view);
			model.playAttack();
		}

		if (card.getCardType() == CardType.SHUFFLE) {
			discardCard(cards, view);
			model.playShuffle();
		}

		if (card.getCardType() == CardType.SEE_THE_FUTURE) {
			discardCard(cards, view);
			List<Card> topThreeCards = model.playSeeTheFuture();
			view.showSeeTheFutureScreen();
			view.updateSeeTheFutureCards(
					appModel.getResourceBundle(),
					topThreeCards
			);
		}

		if (card.getCardType() == CardType.TARGETED_ATTACK) {
			view.showTargetedAttackScreen();
			view.updateTargetedAttackPlayers(
					livingOpponents(),
					(targetId) -> {
						discardCard(cards, view);
						view.hideTargetedAttackScreen();
						model.playTargetedAttack(targetId);
						refreshAfterPlay(view, appModel);
					}
			);
		}

		if (card.getCardType() == CardType.FAVOR) {
			ResourceBundle bundle = appModel.getResourceBundle();
			view.showDemandFavorScreen();
			view.updateDemandFavorPlayers(
					livingOpponents(),
					(targetId) -> {
						view.hideDemandFavorScreen();
						view.showGrantFavorScreen();
						view.updateGrantFavorSubTitle(
								bundle,
								model.getPlayerName(targetId),
								model.getLocalPlayerName()
						);
						view.updateFavorCards(
								model.getSelectedHand(targetId),
								(cardIndex) -> {
									discardCard(
											cards,
											view
									);
									view.hideGrantFavorScreen();
									model.playFavor(
											targetId,
											cardIndex
									);
									refreshAfterPlay(
											view,
											appModel
									);
								}
						);
					}
			);
		}

		refreshAfterPlay(view, appModel);
	}

	private void handleExplodingKitten(GameView view, AppModel appModel) {
		if (model.currentPlayerHasDefuse()) {
			view.showDefuseScreen(
					appModel.getResourceBundle(),
					model.getDeckSize() - 1
			);
		} else {
			model.explodeCurrentPlayer();
		}
	}
}
