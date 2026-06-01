package domain;

import java.util.ArrayList;
import java.util.List;

public final class Player {

    private static final String NULL_CARD_KEY = "player.addCardToHand.nullCard";
    private static final String CARD_AT_INVALID_INDEX_KEY = "player.getCardAt.invalidIndex";
    private static final String REMOVE_INVALID_INDEX_KEY = "player.removeCardFromHand.invalidIndex";
    private static final String ALREADY_DEAD_KEY = "player.markDead.alreadyDead";

    private final int playerId;
    private final List<Card> hand;
    private boolean alive;

    public Player(int playerId) {
        this.playerId = playerId;
        this.hand = new ArrayList<>();
        this.alive = true;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void addCardToHand(Card card) {
        if (card == null) {
            throw new IllegalArgumentException(NULL_CARD_KEY);
        }
        hand.add(card);
    }

    public int getHandSize() {
        return hand.size();
    }

    public Card getCardAt(int index) {
        if (index < 0 || index >= hand.size()) {
            throw new IndexOutOfBoundsException(CARD_AT_INVALID_INDEX_KEY);
        }
        return hand.get(index);
    }

    public List<Card> getHand() {
        return new ArrayList<>(hand);
    }

    public Card removeCardFromHand(int index) {
        if (index < 0 || index >= hand.size()) {
            throw new IndexOutOfBoundsException(REMOVE_INVALID_INDEX_KEY);
        }
        return hand.remove(index);
    }

    public boolean hasCard(CardType type) {
        for (Card card : hand) {
            if (card.getCardType() == type) {
                return true;
            }
        }
        return false;
    }

    public int getIndexOfCard(CardType type) {
        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i).getCardType() == type) {
                return i;
            }
        }
        return -1;
    }

    public boolean isAlive() {
        return alive;
    }

    public void markDead() {
        if (!alive) {
            throw new IllegalStateException(ALREADY_DEAD_KEY);
        }
        alive = false;
    }
}
