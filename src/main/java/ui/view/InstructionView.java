package ui.view;

import java.util.ResourceBundle;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class InstructionView extends StackPane {

	private Text instructionsTitle;
	private Button backButton;
	private final VBox ruleSection;

	private final String[] ruleKeys = {"ruleOne", "ruleTwo", "ruleThree", "ruleFour"};

	private static final int instructionsContainerSpacing = 25;
	private static final int instructionsContainerHeight = 650;
	private static final int instructionsContainerWidth = 950;
	private static final int ruleSpacing = 10;
	private static final int ruleSectionSpacing = 30;
	private static final int ruleBodyWrapping = 850;

	public InstructionView() {
		this.getStyleClass().add("root-pane");

		VBox instructionsContainer = new VBox(instructionsContainerSpacing);

		Rectangle clip = new Rectangle();
		clip.widthProperty().bind(instructionsContainer.widthProperty());
		clip.heightProperty().bind(instructionsContainer.heightProperty());

		instructionsContainer.setClip(clip);
		instructionsContainer.setAlignment(Pos.TOP_CENTER);

		instructionsContainer.setPrefHeight(instructionsContainerHeight);
		instructionsContainer.setMaxHeight(instructionsContainerHeight);
		instructionsContainer.setMinHeight(instructionsContainerHeight);

		instructionsContainer.setPrefWidth(instructionsContainerWidth);
		instructionsContainer.setMaxWidth(instructionsContainerWidth);
		instructionsContainer.setMinWidth(instructionsContainerWidth);

		instructionsContainer.getStyleClass().add("comic-panel");

		BorderPane topBar = createTopBar();
		Region separatorLine = new Region();
		separatorLine.getStyleClass().add("thick-black-line");
		this.ruleSection = new VBox(ruleSectionSpacing);
		instructionsContainer.getChildren().addAll(
				topBar, separatorLine, ruleSection
		);

		this.getChildren().addAll(instructionsContainer);

		String stylePath = "/styles/instruction-style.css";
		this.getStylesheets().add(
				getClass().getResource(stylePath).toExternalForm()
		);
	}

	private BorderPane createTopBar() {
		BorderPane topBar = new BorderPane();

		instructionsTitle = new Text();
		backButton = new Button();

		topBar.setLeft(instructionsTitle);
		topBar.setRight(backButton);

		instructionsTitle.getStyleClass().add("rules-title");
		backButton.getStyleClass().add("btn-sec");

		return topBar;
	}

	private VBox createRule(String titleKey, String bodyKey, ResourceBundle bundle) {
		VBox rule = new VBox(ruleSpacing);
		rule.setAlignment(Pos.TOP_LEFT);

		Text titleText = new Text(bundle.getString(titleKey));
		Text bodyText = new Text(bundle.getString(bodyKey));

		titleText.getStyleClass().add("rules-section-title");

		bodyText.getStyleClass().add("rules-body-text");
		bodyText.setWrappingWidth(ruleBodyWrapping);

		rule.getChildren().addAll(titleText, bodyText);
		return rule;
	}

	public void updateDisplay(ResourceBundle bundle) {
		instructionsTitle.setText(bundle.getString("instructionView.title"));
		backButton.setText(bundle.getString("instructionView.back"));

		ruleSection.getChildren().clear();
		for (String key : ruleKeys) {
			String titleKey = "instructionView." + key + ".title";
			String bodyKey = "instructionView." + key + ".body";
			ruleSection.getChildren().add(createRule(titleKey, bodyKey, bundle));
		}
	}

	public void setOnBackAction(Runnable handler) {
		this.backButton.setOnAction(e -> handler.run());
	}
}
