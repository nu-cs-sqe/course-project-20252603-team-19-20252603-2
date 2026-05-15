package domain;

import java.util.ArrayList;
import java.util.List;

public final class Player {

    private static final String NULL_CARD_KEY = "player.addCardToHand.nullCard";
    private static final String CARD_AT_INVALID_INDEX_KEY = "player.getCardAt.invalidIndex";
    private static final String REMOVE_INVALID_INDEX_KEY = "player.removeCardFromHand.invalidIndex";

    private final int playerId;
    private final List<Card> hand;

    public Player(int playerId) {
        this.playerId = playerId;
        this.hand = new ArrayList<>();
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

    public Card removeCardFromHand(int index) {
        if (index < 0 || index >= hand.size()) {
            throw new IndexOutOfBoundsException(REMOVE_INVALID_INDEX_KEY);
        }
        return hand.remove(index);
    }
}
