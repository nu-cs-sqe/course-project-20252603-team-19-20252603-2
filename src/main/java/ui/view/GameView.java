package ui.view;


import java.util.List;
import java.util.ResourceBundle;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import ui.model.PlayerDisplayInfo;

public class GameView extends StackPane {
	private String cardCountText = "";
	private String cardsText = "";
	private String myTurnText = "";

	private BorderPane topBar;
	private HBox playerBar;
	private HBox gamePlaySection;
	private HBox cardSection;
	private VBox feedContainer;
	private ScrollPane scrollPane;

	private Text logoText;
	private Text deckTitleText;
	private Text turnIndicatorText;
	private Text tableChatterTitle;
	private Label deckCountLabel;
	private Label localHandLabel;

	private Button quitButton;
	private Button deck;
	private Button drawCard;

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
	private static final double playerBarTranslateY = -7.5;

	public GameView() {
		this.getStyleClass().add("game-root");

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

		this.getChildren().addAll(gameContainer);

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
		quitButton.getStyleClass().add("quit-btn");
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

	private VBox createPlayer(PlayerDisplayInfo opponent) {
		VBox player = new VBox(playerSpacing);

		VBox playerAvatar = createPlayerAvatar(opponent.getName());
		Text playerAvatarLabel = createPlayerAvatarLabel(opponent.getName());
		Label playerAvatarCardCount = createPlayerAvatarCardCount(opponent.getHandSize());

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
		playerBar.setAlignment(Pos.TOP_CENTER);
		playerBar.setTranslateY(playerBarTranslateY);
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

	private VBox createDiscardDeck() {
		VBox discardDeck = new VBox();
		discardDeck.getStyleClass().add("discard-card");
		return discardDeck;
	}

	private HBox createGamePlaySection() {
		HBox gamePlaySection = new HBox(gamePlaySectionSpacing);
		gamePlaySection.setAlignment(Pos.CENTER);

		VBox drawDeck = createDrawDeck();
		StackPane tableChatter = createTablechatter();
		VBox discardDeck = createDiscardDeck();

		gamePlaySection.getChildren().addAll(
				drawDeck,
				tableChatter,
				discardDeck
		);
		return gamePlaySection;
	}

	private HBox createCardSection() {
		this.localHandLabel = new Label();
		this.localHandLabel.getStyleClass().add("player-hand-label");

		HBox cardSection = new HBox();
		cardSection.setAlignment(Pos.CENTER);
		cardSection.getChildren().add(localHandLabel);
		return new HBox();
	}

	public void updateDisplay(ResourceBundle bundle) {
		logoText.setText(bundle.getString("gameView.logo"));
		quitButton.setText(bundle.getString("gameView.quit"));
		deckTitleText.setText(bundle.getString("gameView.deck"));
		drawCard.setText(bundle.getString("gameView.drawCard"));
		tableChatterTitle.setText(bundle.getString("gameView.tableChatter"));
		cardCountText = bundle.getString("gameView.cardCount");
		cardsText = bundle.getString("gameView.cards");
		myTurnText = bundle.getString("gameView.myTurn");
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

	public void updateTurnIndicator(boolean isLocalPlayerTurn) {
		List<String> styleClasses = turnIndicatorText.getStyleClass();

		if (isLocalPlayerTurn) {
			turnIndicatorText.setText(myTurnText);
			styleClasses.remove("turn-indicator-inactive");
		} else {
			turnIndicatorText.setText("");

			if (!styleClasses.contains("turn-indicator-inactive")) {
				styleClasses.add("turn-indicator-inactive");
			}
		}

		drawCard.setDisable(!isLocalPlayerTurn);
	}

	public void updateCardCount(int cardCount) {
		updateDeckCount(cardCount);
	}

	public void showLocalHand(int handSize, String playerName) {
		String handText = playerName + ": "
				+ handSize + " " + cardCountText;
		localHandLabel.setText(handText);
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

	public void setOnDrawAction(Runnable handler) {
		this.deck.setOnAction(e -> handler.run());
		this.drawCard.setOnAction(e -> handler.run());
	}
}
