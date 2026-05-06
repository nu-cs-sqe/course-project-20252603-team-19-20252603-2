package domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DeckTest {

    @Test
    public void deck_createFullDeck_correctSize(){
        Deck deck = new Deck();

        int expectedSize = 56;

        List<CardType> cards = deck.getCards();

        assertEquals(expectedSize, cards.size());
    }
}