package domain;

public final class Card {

	private static final String NULL_CARD_TYPE_KEY = "card.nullType";

	private final CardType cardType;

	public Card(CardType cardType) {
		if (cardType == null) {
			throw new IllegalArgumentException(NULL_CARD_TYPE_KEY);
		}
		this.cardType = cardType;
	}

	public CardType getCardType() {
		return cardType;
	}
}
