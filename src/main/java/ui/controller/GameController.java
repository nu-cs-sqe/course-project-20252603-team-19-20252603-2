package ui.controller;

import domain.Card;
import domain.CardType;
import java.text.MessageFormat;
import java.util.List;
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
	private static final int CAT_TRIPLE_SIZE = 3;

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
			handleGameOver(appModel, router);
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
		String cardName = handCards.get(0).getCardName(appModel.getResourceBundle());
		String actionTemplate = appModel.getResourceBundle().getString(
				"gameView.playAction"
		);
		return MessageFormat.format(actionTemplate, playerName, cardName);
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

	private void playCatPair(List<CardView> cards, GameView view, AppModel appModel) {
		CardView card = cards.get(0);
		view.showDoubleSpecialComboScreen();
		view.updateCatCardsPlayer(
				livingOpponents(),
				(targetId) -> {
					discardCard(cards, view);
					view.hideCatCardScreen();
					model.playCatPair(
							targetId,
							card.getCardType()
					);
					refreshAfterPlay(view, appModel);
				}
		);
	}

	private void playCatTriple(List<CardView> cards, GameView view, AppModel appModel) {
		CardType selectedType = cards.get(0).getCardType();
		view.showTripleSpecialComboScreen();
		view.updateTripleComboScreen(
				appModel.getResourceBundle(),
				livingOpponents(),
				(targetId, desiredCard) -> {
					discardCard(cards, view);
					view.hideTripleSpecialComboScreen();
					model.playCatTriple(targetId, selectedType, desiredCard);
					refreshAfterPlay(view, appModel);
				}
		);
	}

	private void playSkip(List<CardView> cards, GameView view) {
		discardCard(cards, view);
		model.playSkip();
	}

	private void playReverse(List<CardView> cards, GameView view) {
		discardCard(cards, view);
		model.playReverse();
	}

	private void playAttack(List<CardView> cards, GameView view) {
		discardCard(cards, view);
		model.playAttack();
	}

	private void playShuffle(List<CardView> cards, GameView view) {
		discardCard(cards, view);
		model.playShuffle();
	}

	private void playSeeTheFuture(List<CardView> cards, GameView view, AppModel appModel) {
		discardCard(cards, view);
		List<Card> topThreeCards = model.playSeeTheFuture();
		view.showSeeTheFutureScreen();
		view.updateSeeTheFutureCards(
				appModel.getResourceBundle(),
				topThreeCards
		);
	}

	private void playTargetedAttack(List<CardView> cards, GameView view, AppModel appModel) {
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

	private void playFavor(List<CardView> cards, GameView view, AppModel appModel) {
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

	private void playNope(List<CardView> cards, GameView view) {
		model.playNope();
		discardCard(cards, view);
	}

	private void playCard(List<CardView> cards, GameView view, AppModel appModel) {
		if (cards.size() == CAT_PAIR_SIZE) {
			playCatPair(cards, view, appModel);
			return;
		}

		if (cards.size() == CAT_TRIPLE_SIZE) {
			playCatTriple(cards, view, appModel);
			return;
		}

		CardType card = cards.get(0).getCardType();

		switch (card) {
			case SKIP:
				playSkip(cards, view);
				break;
			case REVERSE:
				playReverse(cards, view);
				break;
			case ATTACK:
				playAttack(cards, view);
				break;
			case SHUFFLE:
				playShuffle(cards, view);
				break;
			case SEE_THE_FUTURE:
				playSeeTheFuture(cards, view, appModel);
				break;
			case TARGETED_ATTACK:
				playTargetedAttack(cards, view, appModel);
				break;
			case FAVOR:
				playFavor(cards, view, appModel);
				break;
			case NOPE:
				playNope(cards, view);
				break;
			default:
				break;
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
