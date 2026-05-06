package domain;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Deck {
    private List<Integer> cards;

    public Deck(){
        cards = new ArrayList<>();

        for (int i = 0;i < 4;i++){
            cards.add(1);
        }

        for (int i = 0;i < 6;i++){
            cards.add(2);
        }

        for (int i = 0;i < 4;i++){
            cards.add(3);
        }

        for (int i = 0;i < 4;i++){
            cards.add(4);
        }

        for (int i = 0;i < 5;i++){
            cards.add(5);
        }

        for (int i = 0;i < 4;i++){
            cards.add(6);
        }

        for (int i = 0;i < 4;i++){
            cards.add(7);
        }

        for (int i = 0;i < 5;i++){
            cards.add(8);
        }

        for (int i = 0;i < 20;i++){
            cards.add(9);
        }
    }

    public List<Integer> getCards(){
        List<Integer> cardsCopy = new ArrayList<>();
        for (int i = 0;i < cards.size();i++) {
            cardsCopy.add(cards.get(i));
        }
        return cardsCopy;
    }
}