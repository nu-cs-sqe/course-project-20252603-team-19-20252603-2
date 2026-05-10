package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {

    private final String name;
    private final List<CardType> hand;
    private boolean alive;

    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.alive = true;
    }

    public String getName() {
        return name;
    }

    public boolean isAlive() {
        return alive;
    }

    public List<CardType> getHand() {
        return Collections.unmodifiableList(hand);
    }

    public void addCard(CardType card) {
        if (card == null) {
            throw new IllegalArgumentException("Card cannot be null");
        }
        hand.add(card);
    }

    public void removeCard(CardType card) {
        if (!hand.contains(card)) {
            throw new IllegalArgumentException("Card not in hand: " + card);
        }
        hand.remove(card);
    }

    public boolean hasCard(CardType card) {
        return hand.contains(card);
    }

    public void explode() {
        if (!alive) {
            throw new IllegalStateException("Player is already dead");
        }
        alive = false;
    }
}