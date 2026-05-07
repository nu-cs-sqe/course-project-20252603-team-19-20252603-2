package domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeckTest {

    @Test
    void deck_createFullDeck_correctSize(){
        Deck deck = new Deck();

        int expectedSize = 56;

        List<Card> cards = deck.getDrawPile();

        assertEquals(expectedSize, cards.size());
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
}
