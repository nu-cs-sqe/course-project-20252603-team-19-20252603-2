package ui.view;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.IntConsumer;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class GameSetupView extends StackPane {

	private List<TextField> textFields;

	private VBox gameSetupContainer;
	private VBox playerNameSection;
	private HBox playerButtonSection;

	private Text gameSetupTitle;
	private Text totalPlayerLabel;
	private Text whosePlayingHeading;
	private Button backButton;
	private Button launchButton;

	private static final int minPlayerCount = 2;
	private static final int maxPlayerCount = 5;
	private static final int gameSetupContainerSpacing = 15;
	private static final int gameSetupContainerHeight = 700;
	private static final int gameSetupContainerWidth = 600;
	private static final int gameSetupTranslateY = -15;
	private static final int playerSelectionSectionSpacing = 5;
	private static final int playerButtonSectionSpacing = 15;
	private static final int playerSectionSpacing = 15;
	private static final int playerNameSectionSpacing = 15;
	private static final int playerNameSpacing = 5;

	public GameSetupView() {
		this.textFields = new ArrayList<>();

		this.getStyleClass().add("root-pane");

		this.gameSetupContainer = new VBox(gameSetupContainerSpacing);

		Rectangle clip = new Rectangle();
		clip.widthProperty().bind(gameSetupContainer.widthProperty());
		clip.heightProperty().bind(gameSetupContainer.heightProperty());

		gameSetupContainer.setClip(clip);
		gameSetupContainer.setAlignment(Pos.TOP_CENTER);

		gameSetupContainer.setPrefHeight(gameSetupContainerHeight);
		gameSetupContainer.setMaxHeight(gameSetupContainerHeight);
		gameSetupContainer.setMinHeight(gameSetupContainerHeight);

		gameSetupContainer.setPrefWidth(gameSetupContainerWidth);
		gameSetupContainer.setMaxWidth(gameSetupContainerWidth);
		gameSetupContainer.setMinWidth(gameSetupContainerWidth);

		gameSetupContainer.getStyleClass().add("setup-panel");

		BorderPane topBar = createTopBar();
		Region separatorLine = new Region();
		separatorLine.getStyleClass().add("thick-black-line");
		VBox playerSelectionSection = createPlayerSelectionSection();
		VBox playerSection = createPlayerSection();
		launchButton = createLaunchButton();

		gameSetupContainer.getChildren().addAll(
				topBar,
				separatorLine,
				playerSelectionSection,
				playerSection,
				launchButton
		);

		for (Node child : gameSetupContainer.getChildren()) {
			child.setTranslateY(gameSetupTranslateY);
		}

		this.getChildren().addAll(gameSetupContainer);

		String stylePath = "/styles/game-setup-style.css";
		this.getStylesheets().add(
				getClass().getResource(stylePath).toExternalForm()
		);
	}

	private BorderPane createTopBar() {
		BorderPane topBar = new BorderPane();

		gameSetupTitle = new Text();
		backButton = new Button();

		topBar.setLeft(gameSetupTitle);
		topBar.setRight(backButton);

		gameSetupTitle.getStyleClass().add("setup-title");
		backButton.getStyleClass().add("btn-sec");

		return topBar;
	}

	private Button createPlayerCountButton(int count, int selectedCount) {
		Button playerButton = new Button(String.valueOf(count));

		if (selectedCount == count) {
			playerButton.getStyleClass().addAll("btn-count", "selected");
		} else {
			playerButton.getStyleClass().addAll("btn-count", "unselected");
		}

		return playerButton;
	}

	public void updatePlayerCountButtons(int selectedCount, IntConsumer onClick) {
		playerButtonSection.getChildren().clear();
		for (int i = minPlayerCount; i <= maxPlayerCount; i++) {
			final int count = i;
			Button btn = createPlayerCountButton(count, selectedCount);
			btn.setOnAction(e -> onClick.accept(count));
			playerButtonSection.getChildren().add(btn);
		}
	}

	private VBox createPlayerSelectionSection() {
		VBox playerSelectionSection = new VBox(playerSelectionSectionSpacing);

		totalPlayerLabel = new Text();
		this.playerButtonSection = new HBox(playerButtonSectionSpacing);
		playerSelectionSection.getChildren().addAll(totalPlayerLabel, playerButtonSection);

		totalPlayerLabel.getStyleClass().add("setup-section-heading");

		return playerSelectionSection;
	}

	private VBox createPlayerName(String playerNumber, ResourceBundle bundle) {
		VBox playerName = new VBox(playerNameSpacing);

		String titleKey = "gameSetupView.player" + playerNumber;
		String placeholderKey = "gameSetupView.player" + playerNumber + ".placeholder";

		Text title = new Text(bundle.getString(titleKey));
		TextField nameField = new TextField();
		nameField.setPromptText(bundle.getString(placeholderKey));

		playerName.getChildren().addAll(title, nameField);

		title.getStyleClass().add("input-label");
		nameField.getStyleClass().add("comic-input");

		textFields.add(nameField);

		return playerName;
	}

	public void updatePlayerNameSection(int selectedCount, ResourceBundle bundle) {
		playerNameSection.getChildren().clear();
		textFields.clear();
		for (int i = 0; i < selectedCount; i++) {
			final String count = String.valueOf(i + 1);
			VBox playerName = createPlayerName(count, bundle);
			playerNameSection.getChildren().add(playerName);
		}
	}

	private VBox createPlayerSection() {
		VBox playerSection = new VBox(playerSectionSpacing);

		whosePlayingHeading = new Text();
		this.playerNameSection = new VBox(playerNameSectionSpacing);
		playerSection.getChildren().addAll(whosePlayingHeading, playerNameSection);

		whosePlayingHeading.getStyleClass().add("setup-section-heading");

		return playerSection;
	}

	private Button createLaunchButton() {
		Button launchButton = new Button();
		launchButton.getStyleClass().add("btn-launch");
		launchButton.setMaxWidth(Double.MAX_VALUE);
		return launchButton;
	}

	public void updateDisplay(ResourceBundle bundle) {
		gameSetupTitle.setText(bundle.getString("gameSetupView.title"));
		backButton.setText(bundle.getString("gameSetupView.back"));
		totalPlayerLabel.setText(bundle.getString("gameSetupView.totalPlayer"));
		whosePlayingHeading.setText(bundle.getString("gameSetupView.whosePlaying"));
		launchButton.setText(bundle.getString("gameSetupView.startGame"));
	}

	public void updateSetupContainerHeight(int heightChange) {
		int currentHeight = (int) this.gameSetupContainer.getHeight();
		int newHeight = currentHeight + heightChange;
		this.gameSetupContainer.setPrefHeight(newHeight);
		this.gameSetupContainer.setMaxHeight(newHeight);
		this.gameSetupContainer.setMinHeight(newHeight);
	}

	public void setOnBackAction(Runnable handler) {
		this.backButton.setOnAction(e -> handler.run());
	}

	public void setOnLaunchAction(Runnable handler) {
		this.launchButton.setOnAction(e -> handler.run());
	}

	public List<String> getRawPlayerNameInputs() {
		List<String> inputs = new ArrayList<>();
		for (TextField textField : textFields) {
			inputs.add(textField.getText());
		}
		return inputs;
	}
}
