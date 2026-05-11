package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
	private static final int numberOfExplodingKittens = 4;
	private static final int numberOfDefuses = 6;
	private static final int numberOfAttacks = 4;
	private static final int numberOfShuffles = 4;
	private static final int numberOfSkips = 4;
	private static final int numberOfSeeTheFutures = 5;
	private static final int numberOfNopes = 5;
	private static final int numberOfFavors = 4;
	private static final int numberOfCatCards = 20;

	private static final int deckSize = 56;

	private static final String EMPTY_DECK_TYPE_KEY = "deck.emptyType";
	private static final String PEEK_TOP_TOO_MANY_KEY = "deck.peekTop.tooManyRequested";
	private static final String NULL_CARD_TYPE_KEY = "card.nullType";
	private static final String PEEK_TOP_NEGATIVE_N_KEY = "deck.peekTop.negativeN";

	private List<Card> drawPile;
	private List<Card> discardPile;

	Deck(List<Card> cards) {
		this.drawPile = new ArrayList<>(cards);
		this.discardPile = new ArrayList<>();
	}

	public Deck() {
		this.drawPile = new ArrayList<>();
		this.discardPile = new ArrayList<>();

		addCards(CardType.EXPLODING_KITTEN, numberOfExplodingKittens);
		addCards(CardType.DEFUSE, numberOfDefuses);
		addCards(CardType.ATTACK, numberOfAttacks);
		addCards(CardType.SHUFFLE, numberOfShuffles);
		addCards(CardType.SKIP, numberOfSkips);
		addCards(CardType.SEE_THE_FUTURE, numberOfSeeTheFutures);
		addCards(CardType.NOPE, numberOfNopes);
		addCards(CardType.FAVOR, numberOfFavors);
		addCards(CardType.CAT_CARDS, numberOfCatCards);
	}

	public Card drawTop() {
		if (drawPile.isEmpty()) {
			throw new IllegalStateException(EMPTY_DECK_TYPE_KEY);
		}
		return drawPile.remove(0);
	}

	public void shuffle() {
		Collections.shuffle(drawPile);
	}

	public List<Card> peekTop(int n) {
		if (n < 0) {
			throw new IllegalArgumentException(PEEK_TOP_NEGATIVE_N_KEY);
		}
		if (n == 0) {
			return new ArrayList<>();
		}
		if (n > drawPile.size()) {
			throw new IllegalStateException(PEEK_TOP_TOO_MANY_KEY);
		}

		List<Card> result = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			result.add(drawPile.get(i));
		}
		return result;
	}

	public void discard(Card card) {
		if (card == null) {
			throw new IllegalArgumentException(NULL_CARD_TYPE_KEY);
		}
		discardPile.add(card);
	}

	private void addCards(CardType cardType, int cardCount) {
		for (int i = 0; i < cardCount; i++) {
			Card card = new Card(cardType);
			drawPile.add(card);
		}
	}


	public int getSize() {
		return drawPile.size();
	}


	public boolean isEmpty() {
		return getSize() == 0;
	}

	// Incorrect code for TDD purposes
	public boolean insertAt(Card card, int index) {
		drawPile.add(0, card);
		return true;
	}


	public List<Card> getDrawPile() {
		return new ArrayList<>(drawPile);
	}

	public List<Card> getDiscardPile() {
		return new ArrayList<>(discardPile);
	}
}