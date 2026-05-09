package domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeckTest {

    @Test
    void deck_createFullDeck_correctSize(){
        Deck deck = new Deck();

        final int FULL_DECK_SIZE = 56;

        List<Card> cards = deck.getDrawPile();

        assertEquals(FULL_DECK_SIZE, cards.size());
    }

    @Test
    void deck_createFullDeck_firstCardIsExplodingKitten(){
        Deck deck = new Deck();

        CardType expectedFirstElement = CardType.EXPLODING_KITTEN;

        List<Card> cards = deck.getDrawPile();

        assertEquals(expectedFirstElement,cards.get(0).getCardType());
    }

    @Test
    void deck_createFullDeck_lastCardIsCatCards(){
        Deck deck = new Deck();

        CardType expectedLastElement = CardType.CAT_CARDS;

        List<Card> cards = deck.getDrawPile();

        assertEquals(expectedLastElement,cards.get(cards.size()-1).getCardType());
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

        assertCards(cards, index, CardType.EXPLODING_KITTEN, 4); index += 4;
        assertCards(cards, index, CardType.DEFUSE, 6); index += 6;
        assertCards(cards, index, CardType.ATTACK, 4); index += 4;
        assertCards(cards, index, CardType.SHUFFLE, 4); index += 4;
        assertCards(cards, index, CardType.SKIP, 4); index += 4;
        assertCards(cards, index, CardType.SEE_THE_FUTURE, 5); index += 5;
        assertCards(cards, index, CardType.NOPE, 5); index += 5;
        assertCards(cards, index, CardType.FAVOR, 4); index += 4;
        assertCards(cards, index, CardType.CAT_CARDS, 20);
    }

    @Test
    void drawTop_emptyDeck_throwsException(){
        Deck deck = new Deck(new ArrayList<>());

        Exception exception = assertThrows(IllegalStateException.class ,() ->{
            deck.drawTop();
        });

        assertEquals("deck.emptyType", exception.getMessage());
    }

    @Test
    void drawTop_sizeOneDeck_returnsCardAndBecomesEmpty() {
        List<Card> oneCardDeck = new ArrayList<>();
        oneCardDeck.add(new Card(CardType.EXPLODING_KITTEN));

        Deck deck = new Deck(oneCardDeck);

        Card drawn = deck.drawTop();

        assertEquals(CardType.EXPLODING_KITTEN, drawn.getCardType());
        assertEquals(0, deck.getDrawPile().size());
    }

    @Test
    void drawTop_sizeTwoDeck_returnsCardAndBecomesSizeOne() {
        List<Card> twoCardDeck = new ArrayList<>();
        twoCardDeck.add(new Card(CardType.EXPLODING_KITTEN));
        twoCardDeck.add(new Card(CardType.DEFUSE));

        Deck deck = new Deck(twoCardDeck);

        Card drawn = deck.drawTop();

        assertEquals(CardType.EXPLODING_KITTEN, drawn.getCardType());
        assertEquals(1, deck.getDrawPile().size());
    }

    @Test
    void drawTop_fullDeck_size56_returnsCardAndBecomesSize55() {
        Deck deck = new Deck();

        Card drawn = deck.drawTop();

        assertEquals(CardType.EXPLODING_KITTEN, drawn.getCardType());
        assertEquals(55, deck.getDrawPile().size());
    }

    @Test
    void shuffle_emptyDeck_remainsEmpty() {
        Deck deck = new Deck(new ArrayList<>());

        deck.shuffle();

        assertEquals(0, deck.getDrawPile().size());
    }

    @Test
    void shuffle_singleCardDeck_remainsUnchanged() {
        List<Card> oneCardDeck = new ArrayList<>();
        oneCardDeck.add(new Card(CardType.EXPLODING_KITTEN));

        Deck deck = new Deck(oneCardDeck);

        List<Card> beforeShuffle = new ArrayList<>(deck.getDrawPile());
        deck.shuffle();
        List<Card> afterShuffle = deck.getDrawPile();

        assertEquals(1, afterShuffle.size());
        assertEquals(beforeShuffle.get(0).getCardType(),
                afterShuffle.get(0).getCardType());
    }

    @Test
    void shuffle_twoCardDeck_preservesAllCards() {
        List<Card> twoCardDeck = new ArrayList<>();
        twoCardDeck.add(new Card(CardType.EXPLODING_KITTEN));
        twoCardDeck.add(new Card(CardType.DEFUSE));

        Deck deck = new Deck(twoCardDeck);

        deck.shuffle();

        List<Card> actual = deck.getDrawPile();

        assertEquals(2, actual.size());

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

        assertEquals(56, shuffledDeck.size());

        Map<CardType, Long> counts = shuffledDeck.stream()
                .collect(Collectors.groupingBy(
                        Card::getCardType,
                        Collectors.counting()
                ));

        assertEquals(4L, counts.get(CardType.EXPLODING_KITTEN));
        assertEquals(6L, counts.get(CardType.DEFUSE));
        assertEquals(4L, counts.get(CardType.ATTACK));
        assertEquals(4L, counts.get(CardType.SHUFFLE));
        assertEquals(4L, counts.get(CardType.SKIP));
        assertEquals(5L, counts.get(CardType.SEE_THE_FUTURE));
        assertEquals(5L, counts.get(CardType.NOPE));
        assertEquals(4L, counts.get(CardType.FAVOR));
        assertEquals(20L, counts.get(CardType.CAT_CARDS));
    }

    @Test
    void peekTop_negativeN_throwsIllegalArgumentException() {
        Deck deck = new Deck(new ArrayList<>());

        Exception exception = assertThrows(IllegalArgumentException.class ,() ->{
            deck.peekTop(-1);
        });

        assertEquals("deck.emptyType", exception.getMessage());
    }
}
