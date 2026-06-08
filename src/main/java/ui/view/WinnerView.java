package ui.view;

import java.util.ResourceBundle;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class WinnerView extends StackPane {
	private Text winnerTitle;
	private Text winnerText;
	private Text winnerDescription;

	private Button playAgainButton;
	private Button mainMenuButton;

	private static final int trophyImageSize = 100;
	private static final int trophyClipWidth = 76;
	private static final int trophyClipHeight = 76;
	private static final int winnerSectionSpacing = 15;
	private static final int winnerDescriptionWrapping = 300;
	private static final String trophyImageUrl =
			"https://raw.githubusercontent.com/hfg-gmuend/openmoji/master/color/618x618/1F3C6.png";

	public WinnerView() {
		this.getStyleClass().add("win-overlay");
		this.setAlignment(Pos.CENTER);

		VBox container = new VBox();
		container.setAlignment(Pos.CENTER);

		StackPane winnerDialog = createWinnerDialog();

		container.getChildren().add(winnerDialog);

		this.getChildren().add(container);
		this.getStylesheets().add(
				getClass().getResource("/styles/winner-style.css").toExternalForm()
		);
	}

	private StackPane createTrophieIcon() {
		Image image = new Image(trophyImageUrl, true);
		ImageView trophyImage = new ImageView(image);
		trophyImage.setFitWidth(trophyImageSize);
		trophyImage.setFitHeight(trophyImageSize);
		trophyImage.setPreserveRatio(true);
		trophyImage.setSmooth(true);
		trophyImage.getStyleClass().add("win-trophy-image");

		Rectangle imageClip = new Rectangle(trophyClipWidth, trophyClipHeight);
		imageClip.setLayoutX((trophyImageSize - trophyClipWidth) / 2.0);
		imageClip.setLayoutY((trophyImageSize - trophyClipHeight) / 2.0);
		trophyImage.setClip(imageClip);

		StackPane trophyFrame = new StackPane(trophyImage);
		trophyFrame.setAlignment(Pos.CENTER);
		trophyFrame.setMinSize(trophyClipWidth, trophyClipHeight);
		trophyFrame.setPrefSize(trophyClipWidth, trophyClipHeight);
		trophyFrame.setMaxSize(trophyClipWidth, trophyClipHeight);

		Rectangle frameClip = new Rectangle(trophyClipWidth, trophyClipHeight);
		trophyFrame.setClip(frameClip);
		return trophyFrame;
	}

	private Text createWinnerTitle() {
		winnerTitle = new Text("GAME OVER!");
		winnerTitle.getStyleClass().add("win-header");
		return winnerTitle;
	}

	private Text createWinnerText() {
		winnerText = new Text();
		winnerText.getStyleClass().add("win-status");
		return winnerText;
	}

	private Text createWinnerDescription() {
		winnerDescription = new Text();
		winnerDescription.getStyleClass().add("win-sub-details");
		winnerDescription.setWrappingWidth(winnerDescriptionWrapping);
		return winnerDescription;
	}

	private VBox createWinnerButtons() {
		VBox winnerButtonBox = new VBox();
		winnerButtonBox.getStyleClass().add("win-button-box");

		playAgainButton = new Button();
		mainMenuButton = new Button();

		playAgainButton.getStyleClass().add("btn-play-again");
		mainMenuButton.getStyleClass().add("btn-menu");

		winnerButtonBox.getChildren().addAll(
				playAgainButton,
				mainMenuButton
		);

		return winnerButtonBox;
	}

	private StackPane createWinnerDialog() {
		StackPane winnerDialogScreen = new StackPane();
		winnerDialogScreen.getStyleClass().add("win-modal-card");
		winnerDialogScreen.setAlignment(Pos.CENTER);

		StackPane trophieIcon = createTrophieIcon();
		Text winnerTitle = createWinnerTitle();
		Text winnerText = createWinnerText();
		Text winnerDescription = createWinnerDescription();
		VBox winnerButtons = createWinnerButtons();

		VBox winnerSection = new VBox(winnerSectionSpacing);
		winnerSection.setAlignment(Pos.CENTER);
		winnerSection.getChildren().addAll(
				trophieIcon,
				winnerTitle,
				winnerText,
				winnerDescription,
				winnerButtons
		);

		winnerDialogScreen.getChildren().add(winnerSection);

		return winnerDialogScreen;
	}

	public void updateDisplay(ResourceBundle bundle) {
		winnerTitle.setText(bundle.getString("winner.title"));
		winnerDescription.setText(bundle.getString("winner.description"));
		playAgainButton.setText(bundle.getString("winner.playAgain"));
		mainMenuButton.setText(bundle.getString("winner.mainMenu"));
	}

	public void updateWinner(ResourceBundle bundle, String player) {
		winnerText.setText(player + " " + bundle.getString("winner.subtitle"));
	}

	public void setOnPlayAgainAction(Runnable handler) {
		this.playAgainButton.setOnAction(e -> handler.run());
	}

	public void setOnMainMenuAction(Runnable handler) {
		this.mainMenuButton.setOnAction(e -> handler.run());
	}
}
