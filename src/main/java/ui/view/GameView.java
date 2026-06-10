package ui.view;


import domain.Card;
import domain.CardType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
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
			"Reverse", "TargetedAttack", "FeralCat",
			"Clone", "SuperSkip", "Bury",
			"PersonalAttack3X"
	};

	private BorderPane topBar;
	private HBox playerBar;
	private HBox gamePlaySection;
	private HBox cardSection;
	private HBox playerHandSection;
	private HBox handScrollContent;
	private ScrollPane handScrollPane;
	private HBox modalCardRow;
	private HBox defuseSliderInfo;
	private VBox feedContainer;
	private VBox discardPile;
	private VBox modalOverlayScreen;
	private VBox modalSection;
	private VBox modalTextBox;
	private VBox modalBody;
	private VBox modalPlayerButtons;
	private VBox defuseOverlayScreen;
	private VBox tripleComboOverlayScreen;
	private VBox tripleComboCard;
	private VBox tripleTargetButtons;
	private VBox defuseSection;
	private VBox defuseSliderBody;
	private ScrollPane scrollPane;
	private ScrollPane modalCardScroll;
	private StackPane discardPileSection;
	private StackPane modalDialogScreen;
	private StackPane defuseDialogScreen;
	private Slider defuseSlider;

	private Text logoText;
	private Text deckTitleText;
	private Text turnIndicatorText;
	private Text tableChatterTitle;
	private Text discardPileFooterText;
	private Text modalTitle;
	private Text modalSubTitle;
	private Text tripleComboHeader;
	private Text tripleComboInstruction;
	private Text guessBoxLabel;
	private Text defuseTitle;
	private Text defuseSliderTitle;
	private Text defuseSliderLow;
	private Text defuseSliderCurrent;
	private Text defuseSliderHigh;
	private Text playerAvatarLabel;
	private String seeTheFutureTitleText;
	private String seeTheFutureSubTitleText;
	private String seeTheFutureDismissText;
	private String targetedAttackTitleText;
	private String targetedAttackSubTitleText;
	private String demandFavorTitleText;
	private String demandFavorSubTitleText;
	private String grantFavorTitleText;
	private String catCardTitleText;
	private String catCardSubTitleText;
	private String defuseTopLabelText;
	private String defuseCurrentLabelText;
	private String defuseBottomLabelText;
	private int defuseSliderMaxIndex;
	private Label deckCountLabel;
	private Label localHandLabel;
	private Label playerAvatarCardCount;

	private Button quitButton;
	private Button deck;
	private Button drawCard;
	private Button playCardButton;
	private Button modalDismissButton;
	private Button defuseButton;
	private Button explodeButton;

	private ComboBox<CardType> cardGuessComboBox;

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
	private static final int targetedAttackTextBoxSpacing = 10;
	private static final int targetedAttackSectionSpacing = 10;
	private static final int demandFavorTextBoxSpacing = 10;
	private static final int demandFavorSectionSpacing = 10;
	private static final int grantFavorTextBoxSpacing = 10;
	private static final int catCardTextBoxSpacing = 10;
	private static final int catCardSectionSpacing = 10;
	private static final int discardCardWidth = 175;
	private static final int discardCardHeight = 260;
	private static final int peekCardWidth = 140;
	private static final int peekCardHeight = 200;
	private static final int targetedAttackDialogWindowHeight = 145;
	private static final int targetedAttackButtonHeight = 60;
	private static final int demandFavorDialogWindowHeight = 145;
	private static final int demandFavorButtonHeight = 60;
	private static final int catCardDialogWindowHeight = 145;
	private static final int catCardButtonHeight = 60;
	private static final int defuseSliderHeight = 24;
	private static final int maxNumberOfCardSelected = 3;
	private static final int targetedAttackTitleWrap = 400;
	private static final int demandFavorTitleWrap = 400;
	private static final int grantFavorTitleWrap = 400;
	private static final int catCardTitleWrap = 400;
	private static final int defuseContentWidth = 300;
	private static final double handScrollPaneInitialValue = 0.5;
	private static final int tripleComboInstructionWordWrap = 320;
	private static final int tripleTargetButtonsSpacing = 8;

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

		modalOverlayScreen = createModalOverlay();
		modalOverlayScreen.setVisible(false);
		modalOverlayScreen.setManaged(false);

		defuseOverlayScreen = createDefuseOverlay();
		defuseOverlayScreen.setVisible(false);
		defuseOverlayScreen.setManaged(false);

		tripleComboOverlayScreen = createTripleComboOverlay();
		tripleComboOverlayScreen.setVisible(false);
		tripleComboOverlayScreen.setManaged(false);

		this.getChildren().addAll(
				gameContainer,
				modalOverlayScreen,
				defuseOverlayScreen,
				tripleComboOverlayScreen
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

		this.handScrollContent = new HBox(playerHandSection);
		this.handScrollContent.setAlignment(Pos.CENTER);
		this.handScrollContent.getStyleClass().add("hand-scroll-content");

		this.handScrollPane = new ScrollPane(handScrollContent);
		handScrollContent.minWidthProperty().bind(
				Bindings.max(
						handScrollPane.widthProperty(),
						playerHandSection.widthProperty()
				)
		);
		handScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		handScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		handScrollPane.setFitToHeight(false);
		handScrollPane.setFitToWidth(false);
		handScrollPane.setPannable(true);
		handScrollPane.vvalueProperty().addListener((
				obs,
				oldVal,
				newVal
		) -> {
			if (newVal.doubleValue() != 0.0) {
				handScrollPane.setVvalue(0.0);
			}
		});
		handScrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
			if (event.getDeltaY() != 0) {
				event.consume();
			}
		});

		handScrollPane.getStyleClass().add("hand-cards-col-2");

		return handScrollPane;
	}

	private void centerHandScrollInitially() {
		Platform.runLater(() -> {
			double viewportWidth = handScrollPane.getViewportBounds().getWidth();
			double contentWidth = handScrollContent.getBoundsInLocal().getWidth();
			if (viewportWidth <= 0 || contentWidth <= viewportWidth) {
				handScrollPane.setHvalue(0);
				return;
			}
			handScrollPane.setHvalue(handScrollPaneInitialValue);
		});
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

	private void createModalText() {
		modalTitle = new Text();
		modalSubTitle = new Text();
		modalTitle.setTextAlignment(TextAlignment.CENTER);
		modalSubTitle.setTextAlignment(TextAlignment.CENTER);
	}

	private void createModalTextBox() {
		modalTextBox = new VBox(seeTheFutureTextBoxSpacing);
		modalTextBox.getStyleClass().add("modal-header-box");
		modalTextBox.setAlignment(Pos.CENTER);
		modalTextBox.getChildren().addAll(modalTitle, modalSubTitle);
	}

	private void createModalBody() {
		modalBody = new VBox();
		modalBody.getStyleClass().add("modal-body");
		modalBody.setAlignment(Pos.CENTER);
	}

	private void createModalDismissButton() {
		modalDismissButton = new Button();
		modalDismissButton.getStyleClass().add("future-dismiss-button");
		modalDismissButton.setAlignment(Pos.CENTER);
		modalDismissButton.setMaxWidth(Double.MAX_VALUE);
	}

	private void createModalCardRow() {
		modalCardRow = new HBox();
		modalCardRow.getStyleClass().add("future-cards-hbox");
		modalCardRow.setMaxWidth(Double.MAX_VALUE);
	}

	private void createModalCardScroll() {
		modalCardScroll = new ScrollPane(modalCardRow);
		modalCardScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		modalCardScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		modalCardScroll.setFitToHeight(true);
		modalCardScroll.setFitToWidth(true);
		modalCardScroll.setPannable(true);
		modalCardScroll.getStyleClass().add("favor-card-scroll");
	}

	private VBox createModalOverlay() {
		VBox overlay = new VBox();
		overlay.getStyleClass().add("card-overlay-backdrop");
		overlay.setAlignment(Pos.CENTER);
		overlay.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

		modalDialogScreen = new StackPane();

		createModalText();
		createModalTextBox();
		createModalBody();
		createModalDismissButton();
		createModalCardRow();
		createModalCardScroll();

		modalPlayerButtons = new VBox();

		modalSection = new VBox(modalTextBox, modalBody, modalDismissButton);
		modalSection.getStyleClass().add("modal-content-section");
		modalDialogScreen.getChildren().add(modalSection);
		overlay.getChildren().add(modalDialogScreen);

		return overlay;
	}

	private void applyModalDialogStyle(
			String dialogStyle, Integer minHeight, Integer maxHeight
	) {
		modalDialogScreen.getStyleClass().clear();
		modalDialogScreen.getStyleClass().add(dialogStyle);
		if (minHeight != null) {
			modalDialogScreen.setMinHeight(minHeight);
			modalDialogScreen.setMaxHeight(maxHeight);
		} else {
			modalDialogScreen.setMinHeight(Region.USE_COMPUTED_SIZE);
			modalDialogScreen.setMaxHeight(Region.USE_COMPUTED_SIZE);
		}
	}

	private void applyModalTextStyle(
			String titleStyle, String subtitleStyle, int subtitleWrap
	) {
		modalTitle.getStyleClass().clear();
		modalTitle.getStyleClass().add(titleStyle);
		modalSubTitle.getStyleClass().clear();
		modalSubTitle.getStyleClass().add(subtitleStyle);
		modalSubTitle.setWrappingWidth(subtitleWrap);
	}

	private void hideModalDismissButton() {
		modalDismissButton.setVisible(false);
		modalDismissButton.setManaged(false);
	}

	private void showModalDismissButton() {
		modalDismissButton.setVisible(true);
		modalDismissButton.setManaged(true);
	}

	private void setModalCardRowStyle(String styleClass) {
		modalCardRow.getStyleClass().clear();
		modalCardRow.getStyleClass().add(styleClass);
	}

	private void setModalPlayerButtonStyle(String styleClass) {
		modalPlayerButtons.getStyleClass().clear();
		modalPlayerButtons.getStyleClass().add(styleClass);
	}

	private void prepareSeeTheFutureModal() {
		modalTextBox.setSpacing(seeTheFutureTextBoxSpacing);
		applyModalDialogStyle("future-dialog-box", null, null);
		applyModalTextStyle(
				"future-title-text", "future-subtitle-text", 0
		);
		modalTitle.setText(seeTheFutureTitleText);
		modalSubTitle.setText(seeTheFutureSubTitleText);
		modalDismissButton.setText(seeTheFutureDismissText);
		modalSection.setSpacing(seeTheFutureSectionSpacing);
		showModalDismissButton();
		modalBody.getChildren().clear();
		modalCardRow.getChildren().clear();
		setModalCardRowStyle("future-cards-hbox");
		modalBody.getChildren().add(modalCardRow);
	}

	private void prepareTargetedAttackModal() {
		modalTextBox.setSpacing(targetedAttackTextBoxSpacing);
		applyModalDialogStyle(
				"targeted-dialog-box",
				targetedAttackDialogWindowHeight,
				targetedAttackDialogWindowHeight
		);
		applyModalTextStyle(
				"targeted-title-text",
				"targeted-subtitle-text",
				targetedAttackTitleWrap
		);
		modalTitle.setText(targetedAttackTitleText);
		modalSubTitle.setText(targetedAttackSubTitleText);
		modalSection.setSpacing(targetedAttackSectionSpacing);
		hideModalDismissButton();
		modalBody.getChildren().clear();
		modalPlayerButtons.getChildren().clear();
		setModalPlayerButtonStyle("targeted-button-vbox");
		modalBody.getChildren().add(modalPlayerButtons);
	}

	private void prepareDemandFavorModal() {
		modalTextBox.setSpacing(demandFavorTextBoxSpacing);
		applyModalDialogStyle(
				"favor-request-box",
				demandFavorDialogWindowHeight,
				demandFavorDialogWindowHeight
		);
		applyModalTextStyle(
				"favor-title-text",
				"favor-subtitle-text",
				demandFavorTitleWrap
		);
		modalTitle.setText(demandFavorTitleText);
		modalSubTitle.setText(demandFavorSubTitleText);
		modalSection.setSpacing(demandFavorSectionSpacing);
		hideModalDismissButton();
		modalBody.getChildren().clear();
		modalPlayerButtons.getChildren().clear();
		setModalPlayerButtonStyle("favor-button-vbox");
		modalBody.getChildren().add(modalPlayerButtons);
	}

	private void prepareGrantFavorModal() {
		modalTextBox.setSpacing(grantFavorTextBoxSpacing);
		applyModalDialogStyle(
				"favor-grant-box",
				demandFavorDialogWindowHeight,
				demandFavorDialogWindowHeight
		);
		applyModalTextStyle(
				"favor-title-text",
				"favor-subtitle-text",
				grantFavorTitleWrap
		);
		modalTitle.setText(grantFavorTitleText);
		modalSection.setSpacing(grantFavorTextBoxSpacing);
		hideModalDismissButton();
		modalBody.getChildren().clear();
		modalCardRow.getChildren().clear();
		setModalCardRowStyle("favor-card-hbox");
		modalBody.getChildren().add(modalCardScroll);
	}

	private void prepareDoubleSpecialComboScreen() {
		modalTextBox.setSpacing(catCardTextBoxSpacing);
		applyModalDialogStyle(
				"catcard-dialog-box",
				catCardDialogWindowHeight,
				catCardDialogWindowHeight
		);
		applyModalTextStyle(
				"catcard-title-text",
				"catcard-subtitle-text",
				catCardTitleWrap
		);
		modalTitle.setText(catCardTitleText);
		modalSubTitle.setText(catCardSubTitleText);
		modalSection.setSpacing(catCardSectionSpacing);
		hideModalDismissButton();
		modalBody.getChildren().clear();
		modalPlayerButtons.getChildren().clear();
		setModalPlayerButtonStyle("opponent-list");
		modalBody.getChildren().add(modalPlayerButtons);
	}

	private VBox createTripleComboOverlay() {
		VBox overlay = new VBox();
		overlay.getStyleClass().add("combo-three-overlay");
		overlay.setAlignment(Pos.CENTER);
		overlay.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

		tripleComboCard = new VBox();
		tripleComboCard.getStyleClass().add("combo-three-card");
		tripleComboCard.setAlignment(Pos.CENTER);

		tripleComboHeader = new Text();
		tripleComboHeader.getStyleClass().add("combo-three-header");
		tripleComboHeader.setTextAlignment(TextAlignment.CENTER);

		tripleComboInstruction = new Text();
		tripleComboInstruction.getStyleClass().add("combo-three-instruction");
		tripleComboInstruction.setTextAlignment(TextAlignment.CENTER);
		tripleComboInstruction.setWrappingWidth(tripleComboInstructionWordWrap);

		VBox guessBox = new VBox();
		guessBox.getStyleClass().add("guess-box-container");

		guessBoxLabel = new Text();
		guessBoxLabel.getStyleClass().add("guess-box-label");

		cardGuessComboBox = new ComboBox<>();
		cardGuessComboBox.getStyleClass().add("combo-box-guess");
		cardGuessComboBox.setMaxWidth(Double.MAX_VALUE);

		guessBox.getChildren().addAll(guessBoxLabel, cardGuessComboBox);

		tripleTargetButtons = new VBox();
		tripleTargetButtons.setSpacing(tripleTargetButtonsSpacing);
		tripleTargetButtons.setAlignment(Pos.CENTER);
		tripleTargetButtons.setMaxWidth(Double.MAX_VALUE);

		tripleComboCard.getChildren().addAll(
				tripleComboHeader,
				tripleComboInstruction,
				guessBox,
				tripleTargetButtons
		);

		overlay.getChildren().add(tripleComboCard);
		return overlay;
	}

	private void showModal() {
		modalOverlayScreen.setVisible(true);
		modalOverlayScreen.setManaged(true);
	}

	private void hideModal() {
		modalOverlayScreen.setVisible(false);
		modalOverlayScreen.setManaged(false);
		modalBody.getChildren().clear();
	}

	private void createDefuseText() {
		defuseTitle = new Text();
		defuseTitle.setTextAlignment(TextAlignment.CENTER);
		defuseTitle.setWrappingWidth(defuseContentWidth);
		defuseTitle.getStyleClass().add("defuse-header");
	}

	private void createDefuseSliderTitle() {
		defuseSliderTitle = new Text();
		defuseSliderTitle.setTextAlignment(TextAlignment.CENTER);
		defuseSliderTitle.setWrappingWidth(defuseContentWidth);
		defuseSliderTitle.getStyleClass().add("slider-label");
	}

	private void createDefuseSlider() {
		defuseSlider = new Slider();
		defuseSlider.getStyleClass().add("depth-slider");
		defuseSlider.setPrefWidth(defuseContentWidth);
		defuseSlider.setMinWidth(defuseContentWidth);
		defuseSlider.setMaxWidth(defuseContentWidth);
		defuseSlider.setMinHeight(defuseSliderHeight);
		defuseSlider.setPrefHeight(defuseSliderHeight);
		defuseSlider.valueProperty().addListener(
				(observable, oldValue, newValue) ->
						updateDefuseSliderLabels()
		);
	}

	private void updateDefuseSliderLabels() {
		int position = (int) defuseSlider.getValue();
		defuseSliderLow.setText(defuseTopLabelText + " (0)");
		defuseSliderCurrent.setText(defuseCurrentLabelText + ": " + position);
		defuseSliderHigh.setText(
				defuseBottomLabelText + " (" + defuseSliderMaxIndex + ")"
		);
	}

	private void createDefuseSliderInfo() {
		defuseSliderInfo = new HBox();
		defuseSliderInfo.setAlignment(Pos.CENTER);
		defuseSliderInfo.setMaxWidth(defuseContentWidth);
		defuseSliderInfo.setPrefWidth(defuseContentWidth);

		defuseSliderLow = new Text();
		defuseSliderCurrent = new Text();
		defuseSliderHigh = new Text();

		defuseSliderLow.getStyleClass().add("stat-text");
		defuseSliderCurrent.getStyleClass().add("stat-text-chosen");
		defuseSliderHigh.getStyleClass().add("stat-text");

		HBox leftColumn = new HBox(defuseSliderLow);
		leftColumn.setAlignment(Pos.CENTER_LEFT);
		HBox.setHgrow(leftColumn, Priority.ALWAYS);

		HBox centerColumn = new HBox(defuseSliderCurrent);
		centerColumn.setAlignment(Pos.CENTER);
		HBox.setHgrow(centerColumn, Priority.ALWAYS);

		HBox rightColumn = new HBox(defuseSliderHigh);
		rightColumn.setAlignment(Pos.CENTER_RIGHT);
		HBox.setHgrow(rightColumn, Priority.ALWAYS);

		defuseSliderInfo.getChildren().addAll(
				leftColumn,
				centerColumn,
				rightColumn
		);
	}

	private void createDefuseSliderBody() {
		defuseSliderBody = new VBox();
		defuseSliderBody.getStyleClass().add("slider-container");
		defuseSliderBody.setAlignment(Pos.CENTER);
		defuseSliderBody.setFillWidth(true);
		defuseSliderBody.setMaxWidth(defuseContentWidth);
		defuseSliderBody.setPrefWidth(defuseContentWidth);

		createDefuseSliderTitle();
		createDefuseSlider();
		createDefuseSliderInfo();

		defuseSliderBody.getChildren().addAll(
				defuseSliderTitle,
				defuseSlider,
				defuseSliderInfo
		);
	}

	private void createDefuseButton() {
		defuseButton = new Button();
		defuseButton.getStyleClass().add("btn-defuse");
		defuseButton.setMaxWidth(defuseContentWidth);
		defuseButton.setPrefWidth(defuseContentWidth);
	}

	private void createExplodeButton() {
		explodeButton = new Button();
		explodeButton.getStyleClass().add("btn-explode");
		explodeButton.setMaxWidth(defuseContentWidth);
		explodeButton.setPrefWidth(defuseContentWidth);
	}

	private VBox createDefuseOverlay() {
		VBox overlay = new VBox();
		overlay.getStyleClass().add("defuse-overlay");
		overlay.setAlignment(Pos.CENTER);
		overlay.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

		createDefuseText();
		createDefuseSliderBody();
		createDefuseButton();
		createExplodeButton();

		defuseDialogScreen = new StackPane();
		defuseDialogScreen.setAlignment(Pos.CENTER);
		defuseDialogScreen.getStyleClass().add("defuse-modal-card");

		defuseSection = new VBox();
		defuseSection.getStyleClass().add("defuse-section");
		defuseSection.setAlignment(Pos.CENTER);
		defuseSection.setFillWidth(false);
		defuseSection.getChildren().addAll(
				defuseTitle,
				defuseSliderBody,
				defuseButton,
				explodeButton
		);

		defuseDialogScreen.getChildren().add(defuseSection);
		overlay.getChildren().add(defuseDialogScreen);

		return overlay;
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
		seeTheFutureTitleText = bundle.getString("seeTheFuture.title");
		seeTheFutureSubTitleText = bundle.getString("seeTheFuture.subTitle");
		seeTheFutureDismissText = bundle.getString("seeTheFuture.dismissButton");
		targetedAttackTitleText = bundle.getString("targetedAttack.title");
		targetedAttackSubTitleText = bundle.getString("targetedAttack.subTitle");
		demandFavorTitleText = bundle.getString("favor.demandTitle");
		demandFavorSubTitleText = bundle.getString("favor.demandSubTitle");
		grantFavorTitleText = bundle.getString("favor.grantTitle");
		catCardTitleText = bundle.getString("catCardSpecialCombo.title");
		catCardSubTitleText = bundle.getString("catCardSpecialCombo.subTitle");
		defuseTitle.setText(bundle.getString("defuse.title"));
		defuseSliderTitle.setText(bundle.getString("defuse.sliderLabel"));
		defuseButton.setText(bundle.getString("defuse.defuseButton"));
		explodeButton.setText(bundle.getString("defuse.explodeButton"));
		defuseTopLabelText = bundle.getString("defuse.topLabel");
		defuseCurrentLabelText = bundle.getString("defuse.currentLabel");
		defuseBottomLabelText = bundle.getString("defuse.bottomLabel");
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

	public void updateGrantFavorSubTitle(
			ResourceBundle bundle, String fromPlayer, String toPlayer
	) {
		String subTitle = fromPlayer + " "
				+ bundle.getString("favor.grantSubTitle") + " "
				+ toPlayer;
		modalSubTitle.setText(subTitle);
	}

	public void updatePlayerCards(List<Card> hand) {
		this.playerHandSection.getChildren().clear();
		for (Card card : hand) {
			addPlayerCard(card);
		}
		centerHandScrollInitially();
	}

	public void updateFavorCards(List<Card> hand, IntConsumer handler) {
		this.modalCardRow.getChildren().clear();
		for (int idx = 0; idx < hand.size(); idx++) {
			addFavorCard(hand.get(idx), handler, idx);
		}
	}

	public void showSeeTheFutureScreen() {
		prepareSeeTheFutureModal();
		showModal();
	}

	public void hideSeeTheFutureScreen() {
		hideModal();
	}

	public void showTargetedAttackScreen() {
		prepareTargetedAttackModal();
		showModal();
	}

	public void hideTargetedAttackScreen() {
		hideModal();
	}

	public void showDemandFavorScreen() {
		prepareDemandFavorModal();
		showModal();
	}

	public void hideDemandFavorScreen() {
		hideModal();
	}

	public void showGrantFavorScreen() {
		prepareGrantFavorModal();
		showModal();
	}

	public void hideGrantFavorScreen() {
		hideModal();
	}

	public void showDoubleSpecialComboScreen() {
		prepareDoubleSpecialComboScreen();
		showModal();
	}

	public void hideCatCardScreen() {
		hideModal();
	}

	public void showTripleSpecialComboScreen() {
		tripleComboOverlayScreen.setVisible(true);
		tripleComboOverlayScreen.setManaged(true);
	}

	public void hideTripleSpecialComboScreen() {
		tripleComboOverlayScreen.setVisible(false);
		tripleComboOverlayScreen.setManaged(false);
	}

	private Button createTargetPlayerButton(
			PlayerDisplayInfo player,
			CardType desiredCard,
			BiConsumer<Integer, CardType> onTargetSelected
	) {
		String playerName = player.getName();
		int playerId = player.getPlayerId();

		Button playerButton = new Button(
				playerName
		);
		playerButton.getStyleClass().add("btn-three-target");
		playerButton.setMaxWidth(Double.MAX_VALUE);
		playerButton.setOnAction(e -> {
			onTargetSelected.accept(playerId, desiredCard);
		});
		return playerButton;
	}

	public void updateTripleComboScreen(
			ResourceBundle bundle,
			List<PlayerDisplayInfo> opponents,
			BiConsumer<Integer, CardType> onTargetSelected
	) {
		tripleComboHeader.setText(catCardTitleText);
		tripleComboInstruction.setText(catCardSubTitleText);
		guessBoxLabel.setText(bundle.getString("catCardSpecialCombo.guessLabel"));

		cardGuessComboBox.getItems().clear();
		for (CardType type : CardType.values()) {
			if (type != CardType.EXPLODING_KITTEN) {
				cardGuessComboBox.getItems().add(type);
			}
		}
		cardGuessComboBox.setValue(CardType.DEFUSE);

		tripleTargetButtons.getChildren().clear();
		for (PlayerDisplayInfo player : opponents) {
			CardType desiredCard = cardGuessComboBox.getValue();
			Button playerButton = createTargetPlayerButton(
					player,
					desiredCard,
					onTargetSelected
			);
			tripleTargetButtons.getChildren().add(playerButton);
		}
	}

	private void updateDefuseSlider(int deckSize) {
		int maxIndex = Math.max(deckSize, 0);
		defuseSliderMaxIndex = maxIndex;
		defuseSlider.setMin(0);
		defuseSlider.setMax(maxIndex);
		defuseSlider.setValue(0);
		defuseSlider.setBlockIncrement(1);
		defuseSlider.setMajorTickUnit(1);
		defuseSlider.setMinorTickCount(0);
		defuseSlider.setSnapToTicks(true);
		defuseSlider.setDisable(false);
		updateDefuseSliderLabels();
	}

	public void showDefuseScreen(ResourceBundle bundle, int deckSize) {
		defuseTopLabelText = bundle.getString("defuse.topLabel");
		defuseCurrentLabelText = bundle.getString("defuse.currentLabel");
		defuseBottomLabelText = bundle.getString("defuse.bottomLabel");

		updateDefuseSlider(deckSize);
		updateDefuseSliderLabels();

		defuseOverlayScreen.setVisible(true);
		defuseOverlayScreen.setManaged(true);
	}

	public void hideDefuseScreen() {
		defuseOverlayScreen.setVisible(false);
		defuseOverlayScreen.setManaged(false);
	}

	public void updateSeeTheFutureCards(ResourceBundle bundle, List<Card> cards) {
		modalCardRow.getChildren().clear();
		for (Card card : cards) {
			String cardName = cardCollection.get(card.getCardType());
			CardView peekCard = new CardView(cardName);
			peekCard.getStyleClass().remove("hand-card");
			peekCard.getStyleClass().add(
					"future-peeked-card"
			);

			preprocessCard(peekCard, peekCardWidth, peekCardHeight);

			modalCardRow.getChildren().add(peekCard);
		}
	}

	public void updateTargetedAttackPlayers(
			List<PlayerDisplayInfo> players, IntConsumer handler
	) {
		int newHeight = players.size()
				* targetedAttackButtonHeight
				+ targetedAttackDialogWindowHeight;
		modalDialogScreen.setMinHeight(newHeight);
		modalDialogScreen.setMaxHeight(newHeight);

		modalPlayerButtons.getChildren().clear();
		for (PlayerDisplayInfo player : players) {
			String playerName = player.getName();
			int playerId = player.getPlayerId();

			Button playerButton = new Button(playerName);
			playerButton.getStyleClass().add("targeted-action-button");
			playerButton.setOnAction(e -> handler.accept(playerId));

			modalPlayerButtons.getChildren().add(
					playerButton
			);
		}
	}

	public void updateDemandFavorPlayers(
			List<PlayerDisplayInfo> players, IntConsumer handler
	) {
		int newHeight = players.size()
				* demandFavorButtonHeight
				+ demandFavorDialogWindowHeight;
		modalDialogScreen.setMinHeight(newHeight);
		modalDialogScreen.setMaxHeight(newHeight);

		modalPlayerButtons.getChildren().clear();
		for (PlayerDisplayInfo player : players) {
			String playerName = player.getName();
			int playerId = player.getPlayerId();

			Button playerButton = new Button(playerName);
			playerButton.getStyleClass().add("favor-target-button");
			playerButton.setOnAction(e -> handler.accept(playerId));

			modalPlayerButtons.getChildren().add(
					playerButton
			);
		}
	}

	public void updateCatCardsPlayer(
			List<PlayerDisplayInfo> players, IntConsumer handler
	) {
		int newHeight = players.size()
				* catCardButtonHeight
				+ catCardDialogWindowHeight;
		modalDialogScreen.setMinHeight(newHeight);
		modalDialogScreen.setMaxHeight(newHeight);

		modalPlayerButtons.getChildren().clear();
		for (PlayerDisplayInfo player : players) {
			String playerName = player.getName();
			int playerId = player.getPlayerId();

			Button playerButton = new Button(playerName);
			playerButton.getStyleClass().add("catCard-target-button");
			playerButton.setOnAction(e -> handler.accept(playerId));

			modalPlayerButtons.getChildren().add(
					playerButton
			);
		}
	}

	private void handleExtraCard() {
		if (this.selectedHandCards.size() > maxNumberOfCardSelected) {
			CardView card = this.selectedHandCards.get(0);
			card.getStyleClass().remove("hand-card-selected");
			card.getStyleClass().add("hand-card");
			this.selectedHandCards.remove(0);
		}
	}

	private void selectCard(CardView card) {
		card.getStyleClass().remove("hand-card");
		card.getStyleClass().add("hand-card-selected");
		this.selectedHandCards.add(card);
		this.playCardButton.setDisable(false);
		handleExtraCard();
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

	private void addFavorCard(Card card, IntConsumer handler, int index) {
		String assetFolder = cardCollection.get(card.getCardType());
		CardView favorCard = new CardView(assetFolder);
		favorCard.getStyleClass().remove("hand-card");
		favorCard.getStyleClass().add("favor-select-card");
		favorCard.setOnMouseClicked(e -> handler.accept(index));
		this.modalCardRow.getChildren().add(favorCard);
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
		preprocessCard(card, discardCardWidth, discardCardHeight);

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

	private void preprocessCard(CardView card, int width, int height) {
		card.setMaxSize(width, height);
		card.setMinSize(width, height);
		card.setPrefSize(width, height);

		ImageView imageView = (ImageView) card.getChildren().get(0);
		imageView.setFitWidth(width);
		imageView.setFitHeight(height);
		imageView.setPreserveRatio(false);

		Rectangle clip = (Rectangle) imageView.getClip();
		clip.setWidth(width);
		clip.setHeight(height);

		imageView.setClip(clip);
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
		this.modalDismissButton.setOnAction(e -> {
			handler.run();
		});
	}

	public void setOnDefuseButton(IntConsumer handler) {
		defuseButton.setOnAction(event -> handler.accept(
				(int) defuseSlider.getValue()
		));
	}

	public void setOnExplodeButton(Runnable handler) {
		explodeButton.setOnAction(event -> handler.run());
	}
}
