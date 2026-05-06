package domain;

import java.util.ArrayList;
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

    private List<CardType> cards;

    public Deck(){
        cards = new ArrayList<>();

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

    private void addCards(CardType cardType, int cardCount){
        for (int i = 0;i < cardCount;i++){
            cards.add(cardType);
        }
    }

    public List<CardType> getCards(){
        List<CardType> cardsCopy = new ArrayList<>();
        for (int i = 0;i < cards.size();i++) {
            cardsCopy.add(cards.get(i));
        }
        return cardsCopy;
    }
}