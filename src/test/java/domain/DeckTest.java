package domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
