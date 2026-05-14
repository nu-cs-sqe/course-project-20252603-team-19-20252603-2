package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class CardTest {

	@ParameterizedTest
	@EnumSource(CardType.class)
	void getCardType_returnsTypePassedAtConstruction(CardType type) {
		Card card = new Card(type);
		assertEquals(type, card.getCardType());
	}

	@Test
	void constructor_throwsIllegalArgumentExceptionOnNullCardType() {
		IllegalArgumentException ex = assertThrows(
				IllegalArgumentException.class,
				() -> new Card(null)
		);
		assertEquals("card.nullType", ex.getMessage());
	}
}
