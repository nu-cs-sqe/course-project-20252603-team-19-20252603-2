package domain;

public class Card {

	private static final String NULL_CARD_TYPE_EXCEPTION = "cardType must not be null";

	private final CardType cardType;

	public Card(CardType cardType) {
		if (cardType == null) {
			throw new IllegalArgumentException(NULL_CARD_TYPE_EXCEPTION);
		}
		this.cardType = cardType;
	}

	public CardType getCardType() {
		return cardType;
	}
}
