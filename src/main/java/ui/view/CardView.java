package ui.view;

import domain.CardServices;
import domain.CardType;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class CardView extends StackPane {
	private final Map<String, Integer> imageCountDict = Map.ofEntries(
			Map.entry("Attack", 4),
			Map.entry("Bury", 2),
			Map.entry("CatCards", 5),
			Map.entry("Clone", 3),
			Map.entry("Defuse", 6),
			Map.entry("ExplodingKitten", 4),
			Map.entry("Favor", 4),
			Map.entry("FeralCat", 1),
			Map.entry("Nope", 5),
			Map.entry("PersonalAttack3X", 4),
			Map.entry("Reverse", 6),
			Map.entry("SeeTheFuture", 5),
			Map.entry("Shuffle", 4),
			Map.entry("Skip", 4),
			Map.entry("SuperSkip", 4),
			Map.entry("TargetedAttack", 5)
	);
	private final Map<String, CardType> cardNameToType = Map.ofEntries(
			Map.entry("Attack", CardType.ATTACK),
			Map.entry("Bury", CardType.BURY),
			Map.entry("CatCards", CardType.CAT_CARDS),
			Map.entry("Clone", CardType.CLONE),
			Map.entry("Defuse", CardType.DEFUSE),
			Map.entry("ExplodingKitten", CardType.EXPLODING_KITTEN),
			Map.entry("Favor", CardType.FAVOR),
			Map.entry("FeralCat", CardType.FERAL_CAT),
			Map.entry("Nope", CardType.NOPE),
			Map.entry("PersonalAttack3X", CardType.PERSONAL_ATTACK_3X),
			Map.entry("Reverse", CardType.REVERSE),
			Map.entry("SeeTheFuture", CardType.SEE_THE_FUTURE),
			Map.entry("Shuffle", CardType.SHUFFLE),
			Map.entry("Skip", CardType.SKIP),
			Map.entry("SuperSkip", CardType.SUPER_SKIP),
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