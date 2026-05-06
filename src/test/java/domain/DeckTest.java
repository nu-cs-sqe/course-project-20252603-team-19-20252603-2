package domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeckTest {

    @Test
    void deck_createFullDeck_correctSize(){
        Deck deck = new Deck();

        int expectedSize = 56;

        List<CardType> cards = deck.getCards();

        assertEquals(expectedSize, cards.size());
    }

    @Test
    void deck_createFullDeck_firstCardIsExplodingKitten(){
        Deck deck = new Deck();

        CardType expectedFirstElement = CardType.EXPLODING_KITTEN;

        List<CardType> cards = deck.getCards();

        assertEquals(expectedFirstElement,cards.get(0));
    }
}
