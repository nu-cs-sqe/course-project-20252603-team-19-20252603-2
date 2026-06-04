package ui.view;


import domain.Card;
import domain.CardType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import ui.model.PlayerDisplayInfo;

public class GameView extends StackPane {
	private String cardCountText = "";
	private String cardsText = "";

	private final Map<CardType, String> cardCollection;
	private final String[] cards = {
			"ExplodingKitten", "Defuse", "Attack",
			"Shuffle", "Skip", "SeeTheFuture",
			"Nope", "CatCards", "Favor",
			"Reverse", "TargetedAttack",
	};

	private BorderPane topBar;
	private HBox playerBar;
	private HBox gamePlaySection;
	private HBox cardSection;
	private HBox seeTheFutureCardSection;
	private HBox playerHandSection;
	private VBox feedContainer;
	private VBox discardPile;
	private VBox seeTheFutureScreen;
	private ScrollPane scrollPane;
	private StackPane discardPileSection;

	private Text logoText;
	private Text deckTitleText;
	private Text turnIndicatorText;
	private Text tableChatterTitle;
	private Text discardPileFooterText;
	private Text seeTheFutureTitle;
	private Text seeTheFutureSubTitle;
	private Text playerAvatarLabel;
	private Label deckCountLabel;
	private Label localHandLabel;
	private Label playerAvatarCardCount;

	private Button quitButton;
	private Button deck;
	private Button drawCard;
	private Button playCardButton;
	private Button seeTheFutureDismissButton;

	private List<CardView> selectedHandCards;

	private static final int topBarRightSpacing = 20;
	private static final int playerBarSpacing = 25;
	private static final int playerSpacing = 5;
	private static final int deckInfoSpacing = 20;
	private static final int drawDeckSpacing = 25;
	private static final int gamePlaySectionSpacing = 100;
	private static final int tableChatterInfoSpacing = 5;
	private static final int feedContainerSpacing = 5;
	private static final int tableChatterSectionSpacing = 10;
	private static final int playerEventLogSpacing = 8;
	private static final int seeTheFutureTextBoxSpacing = 10;
	private static final int seeTheFutureSectionSpacing = 20;
	private static final int discardCardWidth = 175;
	private static final int discardCardHeight = 260;
	private static final int peekCardWidth = 140;
	private static final int peekCardHeight = 200;

	public GameView() {
		this.getStyleClass().add("game-root");

		this.cardCollection = new HashMap<>();
		this.selectedHandCards = new ArrayList<>();

		CardType[] types = CardType.values();
		for (int i = 0; i < types.length; i++) {
			this.cardCollection.put(
					types[i],
					cards[i]
			);
		}

		this.topBar = createTopBar();
		VBox playerSection = createPlayerSection();
		this.gamePlaySection = createGamePlaySection();
		this.cardSection = createCardSection();

		VBox gameContainer = new VBox();
		gameContainer.getChildren().addAll(
				topBar,
				playerSection,
				gamePlaySection,
				cardSection
		);

		topBar.getStyleClass().add("game-header");
		playerSection.getStyleClass().add("opponents-bar");
		gamePlaySection.getStyleClass().add("table-felt");
		cardSection.getStyleClass().add("player-hand-bar");

		seeTheFutureScreen = createSeeTheFutureScreen();
		seeTheFutureScreen.setVisible(false);
		seeTheFutureScreen.setManaged(false);

		this.getChildren().addAll(
				gameContainer,
				seeTheFutureScreen
		);

		String stylePath = "/styles/game-style.css";
		this.getStylesheets().add(
				getClass().getResource(stylePath).toExternalForm()
		);

		this.feedContainer.heightProperty().addListener((observable, oldValue, newValue) ->
				this.scrollPane.setVvalue(1.0));
	}

	private HBox createTopBarLeft() {
		HBox topBarLeft = new HBox();

		logoText = new Text();
		logoText.getStyleClass().add("game-header-title");

		topBarLeft.getChildren().add(logoText);
		return topBarLeft;
	}

	private VBox createTurnIndication() {
		VBox turnIndicator = new VBox();
		turnIndicator.getStyleClass().add("turn-indicator-box");

		this.turnIndicatorText = new Text();
		turnIndicatorText.getStyleClass().add("turn-indicator-text");

		turnIndicator.getChildren().add(turnIndicatorText);

		return turnIndicator;
	}

	private Button createQuitButton() {
		this.quitButton = new Button();
		quitButton.getStyleClass().addAll("quit-btn", "quit-text");

		return quitButton;
	}

	private HBox createTopBarRight() {
		VBox turnIndicator = createTurnIndication();
		Button quitButton = createQuitButton();

		HBox topBarRight = new HBox(topBarRightSpacing);

		topBarRight.getChildren().addAll(
				turnIndicator,
				quitButton
		);

		return topBarRight;
	}

	private BorderPane createTopBar() {
		BorderPane topBar = new BorderPane();

		HBox topBarLeft = createTopBarLeft();
		HBox topBarRight = createTopBarRight();

		topBarLeft.setAlignment(Pos.CENTER);
		topBarRight.setAlignment(Pos.CENTER);

		topBar.setLeft(topBarLeft);
		topBar.setRight(topBarRight);

		return topBar;
	}

	private VBox createPlayerAvatar(String playerName) {
		VBox playerAvatar = new VBox(0);
		String initial = playerName.toUpperCase().substring(0, 1);
		Text playerInitial = new Text(initial);

		playerAvatar.getChildren().add(playerInitial);

		playerInitial.getStyleClass().add("opponent-avatar-text");
		playerAvatar.getStyleClass().add("opponent-avatar");

		return playerAvatar;
	}

	private Text createPlayerAvatarLabel(String playerName) {
		String name = playerName.toUpperCase();
		Text playerAvatarText = new Text(name);
		playerAvatarText.getStyleClass().add("opponent-label");
		return playerAvatarText;
	}

	private Label createPlayerAvatarCardCount(int cardCount) {
		String cards = cardCount + " " + cardCountText;
		Label playerAvatarCardCount = new Label(cards);
		playerAvatarCardCount.getStyleClass().add("opponent-card-count");
		return playerAvatarCardCount;
	}

	private void checkCurrentTurn(PlayerDisplayInfo opponent, VBox player) {
		if (opponent.isCurrentTurn() && opponent.isAlive()) {
			player.getStyleClass().add("opponent-avatar-active");
			player.getStyleClass().add("opponent-active-highlight");
		} else {
			player.getStyleClass().remove("opponent-avatar-active");
			player.getStyleClass().remove("opponent-active-highlight");
		}
	}

	private void checkPlayerAlive(PlayerDisplayInfo opponent, VBox player, VBox playerAvatar) {
		if (opponent.isAlive()) {
			player.getStyleClass().remove("exploded-player-container");
			playerAvatar.getStyleClass().remove("exploded-avatar-circle");
			playerAvatarLabel.getStyleClass().remove("exploded-player-name");
			playerAvatarCardCount.getStyleClass().remove("exploded-status-badge");
		} else {
			player.getStyleClass().add("exploded-player-container");
			playerAvatar.getStyleClass().add("exploded-avatar-circle");
			playerAvatarLabel.getStyleClass().add("exploded-player-name");
			playerAvatarCardCount.getStyleClass().add("exploded-status-badge");
		}
	}

	private VBox createPlayer(PlayerDisplayInfo opponent) {
		VBox player = new VBox(playerSpacing);

		checkCurrentTurn(opponent, player);

		VBox playerAvatar = createPlayerAvatar(opponent.getName());
		playerAvatarLabel = createPlayerAvatarLabel(opponent.getName());
		playerAvatarCardCount = createPlayerAvatarCardCount(opponent.getHandSize());

		checkPlayerAlive(opponent, player, playerAvatar);

		player.getChildren().addAll(
				playerAvatar,
				playerAvatarLabel,
				playerAvatarCardCount
		);
		player.setAlignment(Pos.TOP_CENTER);

		if (opponent.isCurrentTurn()) {
			player.getStyleClass().add("active-turn");
		}

		return player;
	}

	private VBox createPlayerSection() {
		VBox playerSection = new VBox();
		playerSection.setAlignment(Pos.TOP_CENTER);

		this.playerBar = new HBox(playerBarSpacing);
		playerBar.setAlignment(Pos.BOTTOM_CENTER);
		playerSection.getChildren().add(playerBar);
		return playerSection;
	}

	private VBox createDeckInfo() {
		VBox deckInfo = new VBox(deckInfoSpacing);
		deckInfo.setMouseTransparent(true);

		deckTitleText = new Text();
		this.deckCountLabel = new Label();

		deckTitleText.getStyleClass().add("deck-title-text");
		deckCountLabel.getStyleClass().add("deck-pill-badge");

		deckInfo.getChildren().addAll(
				deckTitleText,
				deckCountLabel
		);
		deckTitleText.setTextAlignment(TextAlignment.CENTER);
		deckInfo.setAlignment(Pos.CENTER);

		return deckInfo;
	}

	private StackPane createDeck(VBox infoOverlay) {
		this.deck = new Button();
		this.deck.setStyle("-fx-background-color: transparent");
		this.deck.setMaxWidth(Double.MAX_VALUE);
		this.deck.setMaxHeight(Double.MAX_VALUE);

		infoOverlay.setMouseTransparent(true);
		infoOverlay.setStyle("-fx-background-color: transparent");

		StackPane topCardGroup = new StackPane();
		topCardGroup.getStyleClass().add("deck-card-button");
		topCardGroup.getChildren().addAll(this.deck, infoOverlay);

		Region underlay1 = new Region();
		underlay1.getStyleClass().add("deck-stack-underlay-1");
		underlay1.setMouseTransparent(true);

		Region underlay2 = new Region();
		underlay2.getStyleClass().add("deck-stack-underlay-2");
		underlay2.setMouseTransparent(true);

		StackPane finalDeckStack = new StackPane();
		finalDeckStack.setAlignment(Pos.TOP_LEFT);
		finalDeckStack.getChildren().addAll(underlay2, underlay1, topCardGroup);

		return finalDeckStack;
	}

	private Button createDrawCard() {
		this.drawCard = new Button();
		drawCard.getStyleClass().add("draw-action-button");
		drawCard.setMaxHeight(Double.MAX_VALUE);
		drawCard.setMaxWidth(Double.MAX_VALUE);
		return this.drawCard;
	}

	private VBox createDrawDeck() {
		VBox drawDeck = new VBox(drawDeckSpacing);
		drawDeck.setAlignment(Pos.CENTER);

		VBox infoOverlay = createDeckInfo();
		StackPane deckStack = createDeck(infoOverlay);
		Button drawCardButton = createDrawCard();

		drawDeck.getChildren().addAll(
				deckStack,
				drawCardButton
		);
		return drawDeck;
	}

	private Text createTableChatterTitle() {
		this.tableChatterTitle = new Text();
		tableChatterTitle.getStyleClass().add("table-chatter-title");
		return tableChatterTitle;
	}

	private VBox createTableChatterInfo() {
		VBox tableChatterInfo = new VBox(tableChatterInfoSpacing);
		Text tableChatterTitle = createTableChatterTitle();
		Region separatorLine = new Region();

		tableChatterTitle.getStyleClass().add("table-chatter-title");
		separatorLine.getStyleClass().add("thick-black-line");

		tableChatterInfo.getChildren().addAll(
				tableChatterTitle,
				separatorLine
		);

		return tableChatterInfo;
	}

	private VBox createTableChatterView() {
		VBox tableChatterView = new VBox();
		tableChatterView.setStyle("-fx-background-color: transparent;");

		this.scrollPane = new ScrollPane();
		this.scrollPane.setFitToWidth(true);
		this.scrollPane.setFitToHeight(true);
		this.scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		this.scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

		this.scrollPane.getStyleClass().add("scroll-pane");

		this.feedContainer = new VBox(feedContainerSpacing);
		this.feedContainer.setStyle("-fx-background-color: transparent;");
		this.scrollPane.setContent(this.feedContainer);

		tableChatterView.getChildren().add(this.scrollPane);

		VBox.setVgrow(this.scrollPane, Priority.ALWAYS);
		VBox.setVgrow(tableChatterView, Priority.ALWAYS);

		return tableChatterView;
	}

	private StackPane createTablechatter() {
		StackPane tableChatter = new StackPane();
		StackPane.setAlignment(tableChatter, Pos.CENTER);
		tableChatter.getStyleClass().add("table-chatter-card");

		VBox tableChatterSection = new VBox(tableChatterSectionSpacing);
		VBox tableChatterInfo = createTableChatterInfo();
		VBox tableChatterView = createTableChatterView();

		tableChatterSection.getChildren().addAll(
				tableChatterInfo,
				tableChatterView
		);

		tableChatter.getChildren().add(
				tableChatterSection
		);

		return tableChatter;
	}

	private StackPane createDiscardPile() {
		this.discardPileSection = new StackPane();
		discardPileSection.setAlignment(Pos.CENTER);

		this.discardPile = new VBox();
		discardPile.setAlignment(Pos.CENTER);
		discardPile.getStyleClass().add("discard-card-pile");

		VBox discardPileFooterBanner = new VBox();
		discardPileFooterBanner.getStyleClass().add("discard-footer-banner");
		discardPileFooterBanner.setAlignment(Pos.BOTTOM_CENTER);

		this.discardPileFooterText = new Text();
		this.discardPileFooterText.getStyleClass().add("discard-footer-text");
		discardPileFooterBanner.getChildren().add(this.discardPileFooterText);

		discardPile.getChildren().add(
				discardPileFooterBanner
		);

		discardPileSection.getChildren().add(
				discardPile
		);

		return discardPileSection;
	}

	private HBox createGamePlaySection() {
		HBox gamePlaySection = new HBox(gamePlaySectionSpacing);
		gamePlaySection.setAlignment(Pos.CENTER);

		VBox drawDeck = createDrawDeck();
		StackPane tableChatter = createTablechatter();
		StackPane discardPile = createDiscardPile();

		gamePlaySection.getChildren().addAll(
				drawDeck,
				tableChatter,
				discardPile
		);
		return gamePlaySection;
	}

	private VBox createHandLabelSection() {
		VBox handLabelSection = new VBox();
		handLabelSection.getStyleClass().add("hand-info-col-1");

		this.localHandLabel = new Label();
		this.localHandLabel.getStyleClass().add("hand-info-title");

		handLabelSection.getChildren().add(
				this.localHandLabel
		);

		return handLabelSection;
	}

	private ScrollPane createPlayerHandSection() {
		this.playerHandSection = new HBox();
		this.playerHandSection.getStyleClass().add("hand-cards-container");

		ScrollPane scrollWrapper = new ScrollPane(playerHandSection);
		scrollWrapper.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scrollWrapper.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scrollWrapper.setFitToHeight(true);
		scrollWrapper.setFitToWidth(true);
		scrollWrapper.setPannable(true);

		scrollWrapper.getStyleClass().add("hand-cards-col-2");

		return scrollWrapper;
	}

	private void createPlayCardButton() {
		this.playCardButton = new Button();
		this.playCardButton.setDisable(true);
		this.playCardButton.getStyleClass().add("btn-play-card");
	}

	private VBox createPlayCardSection() {
		VBox playerCardSection = new VBox();
		playerCardSection.getStyleClass().add("hand-action-col-3");
		playerCardSection.setAlignment(Pos.TOP_CENTER);

		createPlayCardButton();

		playerCardSection.getChildren().add(
				this.playCardButton
		);

		return playerCardSection;
	}

	private HBox createCardSection() {
		VBox handLabelSection = createHandLabelSection();
		ScrollPane playerHandSection = createPlayerHandSection();
		VBox playCardSection = createPlayCardSection();

		HBox cardSection = new HBox();
		cardSection.getChildren().addAll(
				handLabelSection,
				playerHandSection,
				playCardSection
		);

		return cardSection;
	}

	private StackPane createSeeTheFuttureDialogWindow() {
		StackPane seeTheFutureDialogWindow = new StackPane();
		seeTheFutureDialogWindow.getStyleClass().add("future-dialog-box");
		return seeTheFutureDialogWindow;
	}

	private VBox createSeeTheFutureText() {
		VBox seeTheFutureTextBox = new VBox(seeTheFutureTextBoxSpacing);
		seeTheFutureTextBox.setAlignment(Pos.CENTER);

		seeTheFutureTitle = new Text();
		seeTheFutureSubTitle = new Text();

		seeTheFutureTitle.getStyleClass().add("future-title-text");
		seeTheFutureSubTitle.getStyleClass().add("future-subtitle-text");

		seeTheFutureTextBox.getChildren().addAll(
				seeTheFutureTitle,
				seeTheFutureSubTitle
		);

		return seeTheFutureTextBox;
	}

	private HBox createSeeTheFutureCardSection() {
		seeTheFutureCardSection = new HBox();
		seeTheFutureCardSection.getStyleClass().add("future-cards-hbox");
		seeTheFutureCardSection.setMaxWidth(Double.MAX_VALUE);
		return seeTheFutureCardSection;
	}

	private Button createSeeTheFutureDismissButton() {
		seeTheFutureDismissButton = new Button();
		seeTheFutureDismissButton.getStyleClass().add("future-dismiss-button");
		seeTheFutureDismissButton.setAlignment(Pos.CENTER);
		seeTheFutureDismissButton.setMaxWidth(Double.MAX_VALUE);
		return seeTheFutureDismissButton;
	}

	private VBox createSeeTheFutureScreen() {
		VBox seeTheFutureScreen = new VBox();
		seeTheFutureScreen.getStyleClass().add("future-overlay-backdrop");

		StackPane seeTheFutureDialogScreen = createSeeTheFuttureDialogWindow();
		VBox seeTheFutureTextBox = createSeeTheFutureText();
		HBox seeTheFutureCardSection = createSeeTheFutureCardSection();
		Button seeTheFutureDismissButton = createSeeTheFutureDismissButton();

		VBox seeTheFutureSection = new VBox(
				seeTheFutureTextBox,
				seeTheFutureCardSection,
				seeTheFutureDismissButton
		);
		seeTheFutureSection.setSpacing(seeTheFutureSectionSpacing);

		seeTheFutureDialogScreen.getChildren().add(
				seeTheFutureSection
		);

		seeTheFutureScreen.getChildren().add(
				seeTheFutureDialogScreen
		);

		return seeTheFutureScreen;
	}

	public void updateDisplay(ResourceBundle bundle) {
		logoText.setText(bundle.getString("gameView.logo"));
		quitButton.setText(bundle.getString("gameView.quit"));
		deckTitleText.setText(bundle.getString("gameView.deck"));
		drawCard.setText(bundle.getString("gameView.drawCard"));
		tableChatterTitle.setText(bundle.getString("gameView.tableChatter"));
		discardPileFooterText.setText(bundle.getString("gameView.discardPile"));
		playCardButton.setText(bundle.getString("gameView.playCard"));
		cardCountText = bundle.getString("gameView.cardCount");
		cardsText = bundle.getString("gameView.cards");
		seeTheFutureTitle.setText(bundle.getString("seeTheFuture.title"));
		seeTheFutureSubTitle.setText(bundle.getString("seeTheFuture.subTitle"));
		seeTheFutureDismissButton.setText(bundle.getString("seeTheFuture.dismissButton"));
	}

	public void showOpponents(List<PlayerDisplayInfo> opponents) {
		playerBar.getChildren().clear();
		for (PlayerDisplayInfo opponent : opponents) {
			playerBar.getChildren().add(createPlayer(opponent));
		}
	}

	public void updateDeckCount(int count) {
		String cards = count + " " + cardsText;
		deckCountLabel.setText(cards);
	}

	public void updateCardCount(int cardCount) {
		updateDeckCount(cardCount);
	}

	public void updateDrawCount(ResourceBundle bundle, int drawCount) {
		String draws = bundle.getString("gameView.drawCard") + " X" + drawCount;
		drawCard.setText(draws);
	}

	public void updatePlayerTurn(ResourceBundle bundle, String player) {
		String turn = player + " " + bundle.getString("gameView.turn");
		turnIndicatorText.setText(turn);
	}

	public void updateHandCount(int handSize, String playerName) {
		String handText = playerName + ": "
				+ handSize + " " + cardCountText;
		localHandLabel.setText(handText);
	}

	public void updatePlayerCards(List<Card> hand) {
		this.playerHandSection.getChildren().clear();
		for (Card card : hand) {
			addPlayerCard(card);
		}
	}

	public void updateSeeTheFutureScreen(boolean visible) {
		seeTheFutureScreen.setVisible(visible);
		seeTheFutureScreen.setManaged(visible);
	}

	public void updateSeeTheFutureCards(ResourceBundle bundle, List<Card> cards) {
		seeTheFutureCardSection.getChildren().clear();
		for (Card card : cards) {
			String cardName = cardCollection.get(card.getCardType());
			CardView peekCard = new CardView(cardName);
			peekCard.getStyleClass().remove("hand-card");
			peekCard.getStyleClass().add(
					"future-peeked-card"
			);

			peekCard.setMaxSize(peekCardWidth, peekCardHeight);
			peekCard.setMinSize(peekCardWidth, peekCardHeight);
			peekCard.setPrefSize(peekCardWidth, peekCardHeight);

			ImageView imageView = (ImageView) peekCard.getChildren().get(0);
			imageView.setFitWidth(peekCardWidth);
			imageView.setFitHeight(peekCardHeight);
			imageView.setPreserveRatio(false);

			Rectangle clip = (Rectangle) imageView.getClip();
			clip.setWidth(peekCardWidth);
			clip.setHeight(peekCardHeight);

			imageView.setClip(clip);

			seeTheFutureCardSection.getChildren().add(peekCard);
		}
	}

	private void selectCard(CardView card) {
		card.getStyleClass().remove("hand-card");
		card.getStyleClass().add("hand-card-selected");
		this.selectedHandCards.add(card);
		this.playCardButton.setDisable(false);
	}

	private void deselectCard(CardView card) {
		card.getStyleClass().remove("hand-card-selected");
		card.getStyleClass().add("hand-card");
	}

	private void clearSelection() {
		for (CardView card : selectedHandCards) {
			deselectCard(card);
		}
		selectedHandCards.clear();
		this.playCardButton.setDisable(true);
	}

	private void handleCardSelection(CardView playerCard) {
		if (selectedHandCards.contains(playerCard)) {
			deselectCard(playerCard);
			selectedHandCards.remove(playerCard);
			if (selectedHandCards.isEmpty()) {
				this.playCardButton.setDisable(true);
			}
			return;
		}

		CardType currentCardType = playerCard.getCardType();
		if (!selectedHandCards.isEmpty()) {
			CardType anchorType = selectedHandCards.get(0).getCardType();
			if (currentCardType != anchorType) {
				clearSelection();
			}
		}

		selectCard(playerCard);
	}

	public void addPlayerCard(Card card) {
		String assetFolder = cardCollection.get(card.getCardType());
		CardView playerCard = new CardView(assetFolder);
		playerCard.setOnMouseClicked(e -> handleCardSelection(playerCard));
		this.playerHandSection.getChildren().add(playerCard);
	}

	public void clearLog() {
		this.scrollPane.setVvalue(0.0);
		this.feedContainer.getChildren().clear();
	}

	public void addLog(String message) {
		HBox playerEventLog = new HBox(playerEventLogSpacing);
		playerEventLog.getStyleClass().add("chatter-feed-row");
		playerEventLog.setAlignment(Pos.CENTER_LEFT);

		Text logText = new Text(message);
		logText.getStyleClass().add("chatter-feed-text");

		playerEventLog.getChildren().add(logText);
		this.feedContainer.getChildren().add(playerEventLog);
	}

	public void setOnQuitAction(Runnable handler) {
		this.quitButton.setOnAction(e -> handler.run());
	}

	public void clearDiscardCard() {
		this.discardPileSection.getChildren().clear();
		this.discardPileSection.getChildren().add(this.discardPile);
	}

	public void addCardToDiscardPile(CardView card) {
		card.setMaxSize(discardCardWidth, discardCardHeight);
		card.setMinSize(discardCardWidth, discardCardHeight);
		card.setPrefSize(discardCardWidth, discardCardHeight);

		ImageView imageView = (ImageView) card.getChildren().get(0);
		imageView.setFitWidth(discardCardWidth);
		imageView.setFitHeight(discardCardHeight);
		imageView.setPreserveRatio(false);

		Rectangle clip = (Rectangle) imageView.getClip();
		clip.setWidth(discardCardWidth);
		clip.setHeight(discardCardHeight);

		imageView.setClip(clip);

		this.discardPileSection.getChildren().add(
				card
		);
	}

	public void removeCardFromHand() {
		for (CardView card : this.selectedHandCards) {
			this.playerHandSection.getChildren().remove(
					card
			);
		}
		this.selectedHandCards.clear();
		this.playCardButton.setDisable(true);
	}

	public void setOnDrawAction(Runnable handler) {
		this.deck.setOnAction(e -> handler.run());
		this.drawCard.setOnAction(e -> handler.run());
	}

	public void setOnPlayButtonAction(Consumer<List<CardView>> handler) {
		this.playCardButton.setOnAction(e -> {
			handler.accept(new ArrayList<>(this.selectedHandCards));
		});
	}

	public void setOnSeeTheFutureDismissButton(Runnable handler) {
		this.seeTheFutureDismissButton.setOnAction(e -> {
			handler.run();
		});
	}
}
