package domain;

import java.util.List;
import java.util.Random;

public final class ActionController {

    private static final int SEE_THE_FUTURE_COUNT = 3;

    private final Random random;

    public ActionController() {
        this(new Random());
    }

    ActionController(Random random) {
        this.random = random;
    }

    public void shuffleDeck(Deck deck) {
        deck.shuffle();
    }

    public List<Card> peekTopThree(Deck deck) {
        int count = Math.min(SEE_THE_FUTURE_COUNT, deck.getSize());
        return deck.peekTop(count);
    }

    public void reverseDirection(TurnTracker turnTracker) {
        turnTracker.changeCurrentDirection();
    }

    public void giveCard(Player from, Player to, int cardIndex) {
        Card given = from.removeCardFromHand(cardIndex);
        to.addCardToHand(given);
    }

    public void stealRandomCard(Player from, Player to) {
        if (from.getHandSize() == 0) {
            return;
        }
        int index = random.nextInt(from.getHandSize());
        Card stolen = from.removeCardFromHand(index);
        to.addCardToHand(stolen);
    }

    public void stealDesiredCard(Player from, Player to, CardType desiredCard) {
        if (!from.hasCard(desiredCard)) {
            return;
        }
        Card stolen = from.removeCardFromHand(from.getIndexOfCard(desiredCard));
        to.addCardToHand(stolen);
    }
}
