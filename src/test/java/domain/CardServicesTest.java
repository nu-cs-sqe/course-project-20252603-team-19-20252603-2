package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Random;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CardServicesTest {

	private static final String invalidCardNameKey = "cardServices.cardDoesNotExist";

	@ParameterizedTest
	@CsvSource({
			"Attack, 4, /assets/Attack/Attack1.png",
			"CatCards, 5, /assets/CatCards/CatCards1.png",
			"Defuse, 6, /assets/Defuse/Defuse1.png",
			"ExplodingKitten, 4, /assets/ExplodingKitten/ExplodingKitten1.png",
			"Favor, 4, /assets/Favor/Favor1.png",
			"Nope, 5, /assets/Nope/Nope1.png",
			"Reverse, 6, /assets/Reverse/Reverse1.png",
			"SeeTheFuture, 5, /assets/SeeTheFuture/SeeTheFuture1.png",
			"Shuffle, 4, /assets/Shuffle/Shuffle1.png",
			"Skip, 4, /assets/Skip/Skip1.png",
			"TargetedAttack, 5, /assets/TargetedAttack/TargetedAttack1.png",
	})
	void getRandomCardImage_validCardName_returnsFirstFilePath(
			String cardName, int fileCount, String expectedPath
	) {
		Random mockRandom = EasyMock.createMock(Random.class);
		EasyMock.expect(mockRandom.nextInt(fileCount)).andReturn(0);
		EasyMock.replay(mockRandom);

		CardServices cardServices = new CardServices();

		String actualPath = cardServices.getRandomCardImage(
				mockRandom,
				cardName,
				fileCount
		);

		assertEquals(expectedPath, actualPath);

		EasyMock.verify(mockRandom);
	}

	@Test
	void getRandomCardImage_invalidCardName_throwIllegalArgumentException() {
		final int fileCount = 4;
		String cardName = "Empty";

		Random mockRandom = EasyMock.createMock(Random.class);
		EasyMock.expect(mockRandom.nextInt(fileCount)).andReturn(0);
		EasyMock.replay(mockRandom);

		CardServices cardServices = new CardServices();

		IllegalArgumentException ex = assertThrows(
				IllegalArgumentException.class,
				() -> cardServices.getRandomCardImage(
						mockRandom,
						cardName,
						fileCount
				));
		assertEquals(invalidCardNameKey, ex.getMessage());
	}
}