package ui.view;

import domain.CardType;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import ui.CardServices;

public class CardView extends StackPane {
	private final Map<String, Integer> imageCountDict = Map.ofEntries(
			Map.entry("Attack", 4),
			Map.entry("CatCards", 5),
			Map.entry("Defuse", 6),
			Map.entry("ExplodingKitten", 4),
			Map.entry("Favor", 4),
			Map.entry("Nope", 5),
			Map.entry("Reverse", 6),
			Map.entry("SeeTheFuture", 5),
			Map.entry("Shuffle", 4),
			Map.entry("Skip", 4),
			Map.entry("TargetedAttack", 5)
	);
	private final Map<String, CardType> cardNameToType = Map.ofEntries(
			Map.entry("Attack", CardType.ATTACK),
			Map.entry("CatCards", CardType.CAT_CARDS),
			Map.entry("Defuse", CardType.DEFUSE),
			Map.entry("ExplodingKitten", CardType.EXPLODING_KITTEN),
			Map.entry("Favor", CardType.FAVOR),
			Map.entry("Nope", CardType.NOPE),
			Map.entry("Reverse", CardType.REVERSE),
			Map.entry("SeeTheFuture", CardType.SEE_THE_FUTURE),
			Map.entry("Shuffle", CardType.SHUFFLE),
			Map.entry("Skip", CardType.SKIP),
			Map.entry("TargetedAttack", CardType.TARGETED_ATTACK)
	);

	private static final int imageWidth = 120;
	private static final int imageHeight = 160;
	private static final int cardCorner = 12;

	private final String cardName;
	private final CardType cardType;

	public CardView(String card) {
		this.cardName = card;
		this.cardType = cardNameToType.get(card);

		Random random = new Random();
		int imageCount = imageCountDict.get(card);

		String path = CardServices.getRandomCardImage(random, cardName, imageCount);
		String imagePath = getClass().getResource(path).toExternalForm();
		Image image = new Image(imagePath, true);

		ImageView imageView = new ImageView(image);

		imageView.setFitWidth(imageWidth);
		imageView.setFitHeight(imageHeight);
		imageView.setPreserveRatio(false);
		imageView.setSmooth(true);
		imageView.getStyleClass().add("hand-card-image");

		Rectangle clip = new Rectangle(imageWidth, imageHeight);
		clip.setArcWidth(cardCorner);
		clip.setArcHeight(cardCorner);

		imageView.setClip(clip);

		this.getStyleClass().add("hand-card");
		this.getChildren().add(imageView);
	}

	public String getCardName(ResourceBundle bundle) {
		return bundle.getString("cardView." + cardName);
	}

	public CardType getCardType() {
		return cardType;
	}
}