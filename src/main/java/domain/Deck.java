package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private final int NUMBER_OF_EXPLODING_KITTENS = 4;
    private final int NUMBER_OF_DEFUSE = 6;
    private final int NUMBER_OF_ATTACK = 4;
    private final int NUMBER_OF_SHUFFLE = 4;
    private final int NUMBER_OF_SKIP = 4;
    private final int NUMBER_OF_SEE_THE_FUTURE = 5;
    private final int NUMBER_OF_NOPE = 5;
    private final int NUMBER_OF_FAVOR = 4;
    private final int NUMBER_OF_CAT_CARDS = 20;

    private final int DECK_SIZE = 56;

    private static final String EMPTY_DECK_TYPE_KEY = "deck.emptyType";
    private static final String PEEK_TOP_TOO_MANY_KEY = "deck.peekTop.tooManyRequested";

    private List<Card> drawPile;
    private List<Card> discardPile;

    Deck(List<Card> cards){
        this.drawPile = new ArrayList<>(cards);
        this.discardPile = new ArrayList<>();
    }

    public Deck(){
        this.drawPile = new ArrayList<>();
        this.discardPile = new ArrayList<>();

        addCards(CardType.EXPLODING_KITTEN, NUMBER_OF_EXPLODING_KITTENS);
        addCards(CardType.DEFUSE, NUMBER_OF_DEFUSE);
        addCards(CardType.ATTACK, NUMBER_OF_ATTACK);
        addCards(CardType.SHUFFLE, NUMBER_OF_SHUFFLE);
        addCards(CardType.SKIP, NUMBER_OF_SKIP);
        addCards(CardType.SEE_THE_FUTURE, NUMBER_OF_SEE_THE_FUTURE);
        addCards(CardType.NOPE, NUMBER_OF_NOPE);
        addCards(CardType.FAVOR, NUMBER_OF_FAVOR);
        addCards(CardType.CAT_CARDS, NUMBER_OF_CAT_CARDS);
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
            throw new IllegalArgumentException(EMPTY_DECK_TYPE_KEY);
        }
        if (n == 0) {
            return new ArrayList<>();
        }
        if (n > DECK_SIZE) {
            throw new IllegalStateException(PEEK_TOP_TOO_MANY_KEY);
        }

        List<Card> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            result.add(drawPile.get(i));
        }
        return result;
    }

    public void discard(Card card) {
        discardPile.add(card);
    }

    private void addCards(CardType cardType, int cardCount) {
        for (int i = 0; i < cardCount; i++) {
            Card card = new Card(cardType);
            drawPile.add(card);
        }
    }

    public List<Card> getDrawPile(){
        return new ArrayList<>(drawPile);
    }

    public List<Card> getDiscardPile(){
        return new ArrayList<>(discardPile);
    }
}