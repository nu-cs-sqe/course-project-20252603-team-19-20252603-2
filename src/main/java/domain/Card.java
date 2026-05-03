package domain;

public class Card {

	private final CardType cardType;

	public Card(CardType cardType) {
		this.cardType = cardType;
	}

	public CardType getCardType() {
		return cardType;
	}
}
