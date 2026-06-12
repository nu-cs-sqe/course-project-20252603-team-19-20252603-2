package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class DeckTest {

	@Test
	void deck_createFullDeck_correctSize() {
		Deck deck = new Deck();

		final int fullDeckSize = 74;

		List<Card> cards = deck.getDrawPile();

		assertEquals(fullDeckSize, cards.size());
	}

	@Test
	void deck_createFullDeck_firstCardIsExplodingKitten() {
		Deck deck = new Deck();

		CardType expectedFirstElement = CardType.EXPLODING_KITTEN;

		List<Card> cards = deck.getDrawPile();

		assertEquals(expectedFirstElement, cards.get(0).getCardType());
	}

	@Test
	void deck_createFullDeck_lastCardIsCatCards() {
		Deck deck = new Deck();

		CardType expectedLastElement = CardType.CAT_CARDS;

		List<Card> cards = deck.getDrawPile();

		assertEquals(expectedLastElement, cards.get(cards.size() - 1).getCardType());
	}

	private void assertCards(List<Card> cards, int startIndex, CardType type, int count) {
		for (int i = 0; i < count; i++) {
			assertEquals(type, cards.get(startIndex + i).getCardType());
		}
	}

	@Test
	void deck_createFullDeck_containsCardsInCorrectOrder() {
		Deck deck = new Deck();
		List<Card> cards = deck.getDrawPile();

		int index = 0;

		final int numberOfExplodingKitten = 4;
		final int numberOfDefuse = 6;
		final int numberOfAttack = 4;
		final int numberOfShuffle = 4;
		final int numberOfSkip = 4;
		final int numberOfSeeTheFuture = 5;
		final int numberOfNope = 5;
		final int numberOfFavor = 4;
		final int numberOfReverse = 4;
		final int numberOfTargetedAttack = 3;
		final int numberOfFeralCat = 4;
		final int numberOfClone = 2;
		final int numberOfSuperSkip = 2;
		final int numberOfBury = 2;
		final int numberOfPersonalAttack3X = 1;
		final int numberOfCatCards = 20;

		assertCards(cards, index, CardType.EXPLODING_KITTEN, numberOfExplodingKitten);
		index += numberOfExplodingKitten;
		assertCards(cards, index, CardType.DEFUSE, numberOfDefuse);
		index += numberOfDefuse;
		assertCards(cards, index, CardType.ATTACK, numberOfAttack);
		index += numberOfAttack;
		assertCards(cards, index, CardType.SHUFFLE, numberOfShuffle);
		index += numberOfShuffle;
		assertCards(cards, index, CardType.SKIP, numberOfSkip);
		index += numberOfSkip;
		assertCards(cards, index, CardType.SEE_THE_FUTURE, numberOfSeeTheFuture);
		index += numberOfSeeTheFuture;
		assertCards(cards, index, CardType.NOPE, numberOfNope);
		index += numberOfNope;
		assertCards(cards, index, CardType.FAVOR, numberOfFavor);
		index += numberOfFavor;
		assertCards(cards, index, CardType.REVERSE, numberOfReverse);
		index += numberOfReverse;
		assertCards(cards, index, CardType.TARGETED_ATTACK, numberOfTargetedAttack);
		index += numberOfTargetedAttack;
		assertCards(cards, index, CardType.FERAL_CAT, numberOfFeralCat);
		index += numberOfFeralCat;
		assertCards(cards, index, CardType.CLONE, numberOfClone);
		index += numberOfClone;
		assertCards(cards, index, CardType.SUPER_SKIP, numberOfSuperSkip);
		index += numberOfSuperSkip;
		assertCards(cards, index, CardType.BURY, numberOfBury);
		index += numberOfBury;
		assertCards(cards, index, CardType.PERSONAL_ATTACK_3X, numberOfPersonalAttack3X);
		index += numberOfPersonalAttack3X;
		assertCards(cards, index, CardType.CAT_CARDS, numberOfCatCards);
	}

	@Test
	void drawTop_emptyDeck_throwsException() {
		Deck deck = new Deck(new ArrayList<>());

		Exception exception = assertThrows(IllegalStateException.class, () -> {
			deck.drawTop();
		});

		assertEquals("deck.emptyType", exception.getMessage());
	}

	@Test
	void drawTop_sizeOneDeck_returnsCardAndBecomesEmpty() {
		List<Card> oneCardDeck = new ArrayList<>();
		oneCardDeck.add(new Card(CardType.EXPLODING_KITTEN));

		Deck deck = new Deck(oneCardDeck);

		int expectedSize = 0;

		Card drawn = deck.drawTop();

		assertEquals(CardType.EXPLODING_KITTEN, drawn.getCardType());
		assertEquals(expectedSize, deck.getDrawPile().size());
	}

	@Test
	void drawTop_sizeTwoDeck_returnsCardAndBecomesSizeOne() {
		List<Card> twoCardDeck = new ArrayList<>();
		twoCardDeck.add(new Card(CardType.EXPLODING_KITTEN));
		twoCardDeck.add(new Card(CardType.DEFUSE));

		Deck deck = new Deck(twoCardDeck);

		int expectedSize = 1;

		Card drawn = deck.drawTop();

		assertEquals(CardType.EXPLODING_KITTEN, drawn.getCardType());
		assertEquals(expectedSize, deck.getDrawPile().size());
	}

	@Test
	void drawTop_fullDeck_size74_returnsCardAndBecomesSize73() {
		Deck deck = new Deck();

		final int expectedSize = 73;

		Card drawn = deck.drawTop();

		assertEquals(CardType.EXPLODING_KITTEN, drawn.getCardType());
		assertEquals(expectedSize, deck.getDrawPile().size());
	}

	@Test
	void shuffle_emptyDeck_remainsEmpty() {
		Deck deck = new Deck(new ArrayList<>());

		int expectSize = 0;

		deck.shuffle();

		assertEquals(expectSize, deck.getDrawPile().size());
	}

	@Test
	void shuffle_singleCardDeck_remainsUnchanged() {
		List<Card> oneCardDeck = new ArrayList<>();
		oneCardDeck.add(new Card(CardType.EXPLODING_KITTEN));

		Deck deck = new Deck(oneCardDeck);

		int expectedSize = 1;

		List<Card> beforeShuffle = new ArrayList<>(deck.getDrawPile());
		deck.shuffle();
		List<Card> afterShuffle = deck.getDrawPile();

		assertEquals(expectedSize, afterShuffle.size());
		assertEquals(beforeShuffle.get(0).getCardType(),
				afterShuffle.get(0).getCardType());
	}

	@Test
	void shuffle_twoCardDeck_preservesAllCards() {
		List<Card> twoCardDeck = new ArrayList<>();
		twoCardDeck.add(new Card(CardType.EXPLODING_KITTEN));
		twoCardDeck.add(new Card(CardType.DEFUSE));

		Deck deck = new Deck(twoCardDeck);

		int expectSize = 2;

		deck.shuffle();

		List<Card> actual = deck.getDrawPile();

		assertEquals(expectSize, actual.size());

		List<CardType> types = actual.stream()
				.map(Card::getCardType)
				.collect(Collectors.toList());

		assertTrue(types.contains(CardType.EXPLODING_KITTEN));
		assertTrue(types.contains(CardType.DEFUSE));
	}

	@Test
	void shuffle_fullDeck_preservesAllCards() {
		Deck deck = new Deck();

		deck.shuffle();

		List<Card> shuffledDeck = deck.getDrawPile();

		final int expectedSize = 74;

		assertEquals(expectedSize, shuffledDeck.size());

		Map<CardType, Long> counts = shuffledDeck.stream()
				.collect(Collectors.groupingBy(
						Card::getCardType,
						Collectors.counting()
				));

		final Long numberOfExplodingKitten = 4L;
		final Long numberOfDefuse = 6L;
		final Long numberOfAttack = 4L;
		final Long numberOfShuffle = 4L;
		final Long numberOfSkip = 4L;
		final Long numberOfSeeTheFuture = 5L;
		final Long numberOfNope = 5L;
		final Long numberOfFavor = 4L;
		final Long numberOfReverse = 4L;
		final Long numberOfTargetedAttack = 3L;
		final Long numberOfCatCards = 20L;

		assertEquals(numberOfExplodingKitten, counts.get(CardType.EXPLODING_KITTEN));
		assertEquals(numberOfDefuse, counts.get(CardType.DEFUSE));
		assertEquals(numberOfAttack, counts.get(CardType.ATTACK));
		assertEquals(numberOfShuffle, counts.get(CardType.SHUFFLE));
		assertEquals(numberOfSkip, counts.get(CardType.SKIP));
		assertEquals(numberOfSeeTheFuture, counts.get(CardType.SEE_THE_FUTURE));
		assertEquals(numberOfNope, counts.get(CardType.NOPE));
		assertEquals(numberOfFavor, counts.get(CardType.FAVOR));
		assertEquals(numberOfReverse, counts.get(CardType.REVERSE));
		assertEquals(numberOfTargetedAttack, counts.get(CardType.TARGETED_ATTACK));
		assertEquals(numberOfCatCards, counts.get(CardType.CAT_CARDS));
	}

	@Test
	void shuffle_fullDeck_changesOrder() {
		Deck deck = new Deck();

		List<CardType> before = deck.getDrawPile().stream()
				.map(Card::getCardType)
				.collect(Collectors.toList());

		boolean changed = false;

		final int timesPlayed = 10;

		for (int i = 0; i < timesPlayed; i++) {
			deck.shuffle();

			List<CardType> after = deck.getDrawPile().stream()
					.map(Card::getCardType)
					.collect(Collectors.toList());

			if (!before.equals(after)) {
				changed = true;
				break;
			}
		}

		assertTrue(changed);
	}

	@Test
	void peekTop_negativeN_throwsIllegalArgumentException() {
		Deck deck = new Deck(new ArrayList<>());

		final int n = -1;

		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			deck.peekTop(n);
		});

		assertEquals("deck.peekTop.negativeN", exception.getMessage());
	}

	@Test
	void peekTopIsZeroReturnsEmptyList() {
		List<Card> oneCardDeck = new ArrayList<>();
		oneCardDeck.add(new Card(CardType.EXPLODING_KITTEN));

		Deck deck = new Deck(oneCardDeck);

		final int n = 0;

		int expectedDeckSize = 1;
		int expectedResultSize = 0;

		List<Card> result = deck.peekTop(n);

		assertEquals(expectedResultSize, result.size());
		assertEquals(expectedDeckSize, deck.getDrawPile().size()); // deck unchanged
	}

	@Test
	void peekTopIsOneReturnsOneCardList() {
		List<Card> twoCardDeck = new ArrayList<>();
		twoCardDeck.add(new Card(CardType.EXPLODING_KITTEN));
		twoCardDeck.add(new Card(CardType.DEFUSE));

		Deck deck = new Deck(twoCardDeck);

		final int n = 1;

		Card expectedFirstCard = new Card(CardType.EXPLODING_KITTEN);
		int expectedDeckSize = 2;
		int expectedResultSize = 1;

		List<Card> result = deck.peekTop(n);

		assertEquals(expectedResultSize, result.size());
		assertEquals(expectedFirstCard.getCardType(), result.get(0).getCardType());
		assertEquals(expectedDeckSize, deck.getDrawPile().size()); // deck unchanged
	}

	@Test
	void peekTopIsThreeReturnsThreeCards() {
		Deck deck = new Deck();

		final int n = 3;
		List<Card> result = deck.peekTop(n);

		final int expectedDeckSize = 74;
		final int expectedResultSize = 3;

		assertEquals(expectedResultSize, result.size());

		assertEquals(deck.getDrawPile().get(0).getCardType(), result.get(0).getCardType());
		assertEquals(deck.getDrawPile().get(1).getCardType(), result.get(1).getCardType());
		assertEquals(deck.getDrawPile().get(2).getCardType(), result.get(2).getCardType());

		assertEquals(expectedDeckSize, deck.getDrawPile().size());
	}

	@Test
	void peekTopIs55ReturnsFiftyFiveCards() {
		Deck deck = new Deck();

		final int n = 55;

		final int expectedDeckSize = 74;
		final int expectedResultSize = 55;

		List<Card> result = deck.peekTop(n);

		assertEquals(expectedResultSize, result.size());

		for (int i = 0; i < n; i++) {
			assertEquals(
					deck.getDrawPile().get(i).getCardType(),
					result.get(i).getCardType()
			);
		}

		assertEquals(expectedDeckSize, deck.getDrawPile().size());
	}

	@Test
	void peekTopIs74ReturnsAllCards() {
		Deck deck = new Deck();

		final int n = 74;

		final int expectedDeckSize = 74;
		final int expectedResultSize = 74;

		List<Card> result = deck.peekTop(n);

		assertEquals(expectedResultSize, result.size());

		for (int i = 0; i < n; i++) {
			assertEquals(
					deck.getDrawPile().get(i).getCardType(),
					result.get(i).getCardType()
			);
		}

		assertEquals(expectedDeckSize, deck.getDrawPile().size());
	}

	@Test
	void peekTopGreaterThanDeckSizeThrowsIllegalStateException() {
		Deck deck = new Deck();

		final int n = 75;

		Exception exception = assertThrows(IllegalStateException.class, () -> {
			deck.peekTop(n);
		});

		assertEquals("deck.peekTop.tooManyRequested", exception.getMessage());
	}

	@Test
	void peekTopExceedsCurrentSizeThrowsIllegalStateException() {
		List<Card> cards = new ArrayList<>();
		cards.add(new Card(CardType.EXPLODING_KITTEN));
		cards.add(new Card(CardType.DEFUSE));
		cards.add(new Card(CardType.ATTACK));

		Deck deck = new Deck(cards);

		final int n = 5;

		Exception exception = assertThrows(IllegalStateException.class, () -> {
			deck.peekTop(n);
		});

		assertEquals("deck.peekTop.tooManyRequested", exception.getMessage());
	}

	@ParameterizedTest
	@EnumSource(CardType.class)
	void discardAllValidCardTypesAddsToDiscardPile(CardType type) {
		Deck deck = new Deck();

		int expectedDiscardPileSize = 1;

		deck.discard(new Card(type));

		List<Card> discardPile = deck.getDiscardPile();

		assertEquals(expectedDiscardPileSize, discardPile.size());
		assertEquals(type, discardPile.get(0).getCardType());
	}

	@Test
	void discardNullCardThrowsIllegalArgumentException() {
		Deck deck = new Deck();

		int expectDiscardPileSize = 0;

		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			deck.discard(null);
		});

		assertEquals("card.nullType", exception.getMessage());
		assertEquals(expectDiscardPileSize, deck.getDiscardPile().size());
	}


	@Test
	void getSize_InitialDeck_ReturnsDeckSize() {
		Deck deck = new Deck();

		final int fullDeckSize = 74;
		int expectedDeckSize = fullDeckSize;
		int actualDeckSize = deck.getSize();

		assertEquals(expectedDeckSize, actualDeckSize);
	}


	@Test
	void getSize_EmptyDeck_ReturnsZero() {
		Deck deck = new Deck();

		final int fullDeckSize = deck.getSize();
		for (int i = 0; i < fullDeckSize; i++) {
			deck.drawTop();
		}

		final int expectedDeckSize = 0;
		int actualDeckSize = deck.getSize();

		assertEquals(expectedDeckSize, actualDeckSize);
	}


	@Test
	void isEmpty_InitialDeck_ReturnsFalse() {
		Deck deck = new Deck();

		boolean expectedIsEmpty = false;
		boolean actualIsEmpty = deck.isEmpty();

		assertEquals(expectedIsEmpty, actualIsEmpty);
	}


	@Test
	void isEmpty_EmptyDeck_ReturnsTrue() {
		Deck deck = new Deck();

		final int fullDeckSize = deck.getSize();
		for (int i = 0; i < fullDeckSize; i++) {
			deck.drawTop();
		}

		boolean expectedIsEmpty = true;
		boolean actualIsEmpty = deck.isEmpty();

		assertEquals(expectedIsEmpty, actualIsEmpty);
	}


	@Test
	void insertAt_InitialDeck_ReturnsTrue() {
		Deck deck = new Deck();
		Card cardToInsert = new Card(CardType.EXPLODING_KITTEN);

		final int insertIndex = 0;

		boolean expectedIsSuccess = true;
		boolean actualIsSuccess = deck.insertAt(cardToInsert, insertIndex);

		assertEquals(expectedIsSuccess, actualIsSuccess);
		assertTrue(deck.getDrawPile().get(insertIndex) == cardToInsert);
	}


	@Test
	void insertAt_InitialDeckIndex1_ReturnsTrue() {
		Deck deck = new Deck();
		Card cardToInsert = new Card(CardType.EXPLODING_KITTEN);

		final int insertIndex = 1;

		boolean expectedIsSuccess = true;
		boolean actualIsSuccess = deck.insertAt(cardToInsert, insertIndex);

		assertEquals(expectedIsSuccess, actualIsSuccess);
		assertTrue(deck.getDrawPile().get(insertIndex) == cardToInsert);
	}


	@Test
	void insertAt_InitialDeckIndex63_ReturnsTrue() {
		Deck deck = new Deck();
		Card cardToInsert = new Card(CardType.EXPLODING_KITTEN);

		final int insertIndex = 63;

		boolean expectedIsSuccess = true;
		boolean actualIsSuccess = deck.insertAt(cardToInsert, insertIndex);

		assertEquals(expectedIsSuccess, actualIsSuccess);
		assertTrue(deck.getDrawPile().get(insertIndex) == cardToInsert);
	}


	@Test
	void insertAt_InitialDeckIndex75_ThrowsException() {
		Deck deck = new Deck();
		Card cardToInsert = new Card(CardType.EXPLODING_KITTEN);

		final int insertIndex = 75;

		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			deck.insertAt(cardToInsert, insertIndex);
		});

		assertEquals("deck.insertAt.indexTooLarge", exception.getMessage());
	}


	@Test
	void insertAt_InitialDeckIndexNeg1_ThrowsException() {
		Deck deck = new Deck();
		Card cardToInsert = new Card(CardType.EXPLODING_KITTEN);

		final int insertIndex = -1;

		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			deck.insertAt(cardToInsert, insertIndex);
		});

		assertEquals("deck.insertAt.negativeIndex", exception.getMessage());
	}


	@Test
	void insertAt_InitialDeckNullCard_ThrowsException() {
		Deck deck = new Deck();
		Card cardToInsert = null;

		final int insertIndex = 1;

		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			deck.insertAt(cardToInsert, insertIndex);
		});

		assertEquals("card.nullType", exception.getMessage());
	}


	@Test
	void insertAt_InitialDeckNullCardNeg1_ThrowsException() {
		Deck deck = new Deck();
		Card cardToInsert = null;

		final int insertIndex = -1;

		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			deck.insertAt(cardToInsert, insertIndex);
		});

		assertEquals("card.nullType", exception.getMessage());
	}
}
