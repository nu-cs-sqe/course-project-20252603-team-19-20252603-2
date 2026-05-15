package domain;

import java.util.ArrayList;
import java.util.List;

public final class Player {

    private static final String NULL_CARD_KEY = "player.addCardToHand.nullCard";

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
}
