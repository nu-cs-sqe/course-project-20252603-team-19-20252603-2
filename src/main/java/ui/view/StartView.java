package ui.view;

import java.util.ResourceBundle;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class StartView extends StackPane {

	private Text titleExploding;
	private Text titleKittens;

	private Text subTitleCardGame;
	private Text subLine1;
	private Text subLine2;

	private Button startGameButton;
	private Button howToPlayButton;
	private Button languageButton;

	private static final int containerWidth = 500;
	private static final int buttonBoxWidth = 320;
	private static final int containerSpacing = 25;
	private static final int titleBoxSpacing = -10;
	private static final int descriptionBoxSpacing = 10;
	private static final int buttonBoxSpacing = 20;

	public StartView() {
		this.getStyleClass().add("root-pane");

		VBox container = new VBox(containerSpacing);
		container.setAlignment(Pos.CENTER);
		container.setMaxWidth(containerWidth);

		VBox titleBox = createTitle();
		VBox descriptionBox = createDescription();
		VBox buttonBox = createButtons();

		container.getChildren().addAll(
				titleBox, descriptionBox, buttonBox
		);
		this.getChildren().add(container);

		this.getStylesheets().add(
				getClass().getResource("/styles/start-style.css").toExternalForm()
		);
	}

	private VBox createTitle() {
		VBox titleBox = new VBox(titleBoxSpacing);
		titleBox.setAlignment(Pos.CENTER);

		titleExploding = new Text();
		titleExploding.getStyleClass().add("title-exploding");

		titleKittens = new Text();
		titleKittens.getStyleClass().add("title-kittens");

		titleBox.getChildren().addAll(titleExploding, titleKittens);

		return titleBox;
	}

	private VBox createDescription() {
		VBox descriptionBox = new VBox(descriptionBoxSpacing);
		descriptionBox.setAlignment(Pos.CENTER);
		descriptionBox.getStyleClass().add("description-box");

		subTitleCardGame = new Text();
		subTitleCardGame.getStyleClass().add("subtitle-card-game");

		descriptionBox.setSpacing(descriptionBoxSpacing);

		subLine1 = new Text();
		subLine1.getStyleClass().add("sub-line-detail");

		subLine2 = new Text();
		subLine2.getStyleClass().add("sub-line-detail");

		descriptionBox.getChildren().addAll(subTitleCardGame, subLine1, subLine2);

		return descriptionBox;
	}

	private VBox createButtons() {
		VBox buttonBox = new VBox(buttonBoxSpacing);
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.setMaxWidth(buttonBoxWidth);

		startGameButton = new Button();
		startGameButton.getStyleClass().add("btn-main");
		startGameButton.setMaxWidth(Double.MAX_VALUE);

		howToPlayButton = new Button();
		howToPlayButton.getStyleClass().add("btn-sec");
		howToPlayButton.setMaxWidth(Double.MAX_VALUE);

		languageButton = new Button();
		languageButton.getStyleClass().add("btn-sec");
		languageButton.setMaxWidth(Double.MAX_VALUE);

		buttonBox.getChildren().addAll(
				startGameButton, howToPlayButton, languageButton
		);

		return buttonBox;
	}

	public void updateDisplay(ResourceBundle bundle) {
		this.titleExploding.setText(bundle.getString("startView.title.Exploding"));
		this.titleKittens.setText(bundle.getString("startView.title.Kittens"));
		this.subTitleCardGame.setText(bundle.getString("startView.subTitle.CardGame"));
		this.subLine1.setText(bundle.getString("startView.subLine1"));
		this.subLine2.setText(bundle.getString("startView.subLine2"));
		this.startGameButton.setText(bundle.getString("startView.startGameButton"));
		this.howToPlayButton.setText(bundle.getString("startView.howToPlayButton"));
		this.languageButton.setText(bundle.getString("startView.languageButton"));
	}

	public void setOnStartGameAction(Runnable handler) {
		this.startGameButton.setOnAction(e -> handler.run());
	}

	public void setOnHowToPlayAction(Runnable handler) {
		this.howToPlayButton.setOnAction(e -> handler.run());
	}

	public void setOnLanguageAction(Runnable handler) {
		this.languageButton.setOnAction(e -> handler.run());
	}
}
