package domain;

import java.util.List;
import java.util.Random;

public class CardServices {
	private static final String rootPath = "/assets/";
	private static final String[] cards = {
			"ExplodingKitten", "Defuse", "Attack",
			"Shuffle", "Skip", "SeeTheFuture",
			"Nope", "CatCards", "Favor",
			"Reverse", "TargetedAttack", "FeralCat",
			"Clone", "SuperSkip", "Bury",
			"PersonalAttack3X"
	};
	private static final String invalidCardName = "cardServices.cardDoesNotExist";

	private static void validateCardName(String cardName) {
		if (!List.of(cards).contains(cardName)) {
			throw new IllegalArgumentException(invalidCardName);
		}
	}

	public static String getRandomCardImage(Random random, String cardName, int fileCount) {
		validateCardName(cardName);

		String folderPath = rootPath + cardName + "/";

		int randomIndex = random.nextInt(fileCount);

		int fileNumber = randomIndex + 1;
		return folderPath + cardName + fileNumber + ".png";
	}
}
