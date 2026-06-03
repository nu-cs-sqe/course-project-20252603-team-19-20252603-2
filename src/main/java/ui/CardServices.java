package ui;

import java.util.Random;

public class CardServices {
	private static final String rootPath = "/assets/";

	public static String getRandomCardImage(Random random, String cardName, int fileCount) {
		String folderPath = rootPath + cardName + "/";

		int randomIndex = random.nextInt(fileCount);

		int fileNumber = randomIndex + 1;
		return folderPath + cardName + fileNumber + ".png";
	}
}
