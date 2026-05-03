package domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CardTest {

	@ParameterizedTest
	@EnumSource(CardType.class)
	void getCardType_returnsTypePassedAtConstruction(CardType type) {
		Card card = new Card(type);
		assertEquals(type, card.getCardType());
	}
}
