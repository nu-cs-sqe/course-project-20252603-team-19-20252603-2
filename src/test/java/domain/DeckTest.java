package domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    void deck_createFullDeck_containsCardsInCorrectOrder() {
        Deck deck = new Deck();

        List<Card> cards = deck.getDrawPile();

        final int EXPLODING_KITTEN_COUNT = 4;
        final int DEFUSE_COUNT = 6;
        final int ATTACK_COUNT = 4;
        final int SHUFFLE_COUNT = 4;
        final int SKIP_COUNT = 4;
        final int SEE_THE_FUTURE_COUNT = 5;
        final int NOPE_COUNT = 5;
        final int FAVOR_COUNT = 4;
        final int CAT_CARDS_COUNT = 20;

        int index = 0;

        for (int i = 0; i < EXPLODING_KITTEN_COUNT; i++) {
            assertEquals(CardType.EXPLODING_KITTEN, cards.get(index++).getCardType());
        }

        for (int i = 0; i < DEFUSE_COUNT; i++) {
            assertEquals(CardType.DEFUSE, cards.get(index++).getCardType());
        }

        for (int i = 0; i < ATTACK_COUNT; i++) {
            assertEquals(CardType.ATTACK, cards.get(index++).getCardType());
        }

        for (int i = 0; i < SHUFFLE_COUNT; i++) {
            assertEquals(CardType.SHUFFLE, cards.get(index++).getCardType());
        }

        for (int i = 0; i < SKIP_COUNT; i++) {
            assertEquals(CardType.SKIP, cards.get(index++).getCardType());
        }

        for (int i = 0; i < SEE_THE_FUTURE_COUNT; i++) {
            assertEquals(CardType.SEE_THE_FUTURE, cards.get(index++).getCardType());
        }

        for (int i = 0; i < NOPE_COUNT; i++) {
            assertEquals(CardType.NOPE, cards.get(index++).getCardType());
        }

        for (int i = 0; i < FAVOR_COUNT; i++) {
            assertEquals(CardType.FAVOR, cards.get(index++).getCardType());
        }

        for (int i = 0; i < CAT_CARDS_COUNT; i++) {
            assertEquals(CardType.CAT_CARDS, cards.get(index++).getCardType());
        }

        assertEquals(56, cards.size());
    }

    @Test
    void drawTop_emptyDeck_throwsException(){
        Deck deck = new Deck(new ArrayList<>());

        String expectedMsg = "The draw pile is empty";

        Exception exception = assertThrows(IllegalStateException.class ,() ->{
            deck.drawTop();
        });

        assertEquals(expectedMsg, exception.getMessage());
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
}
