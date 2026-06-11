package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

class ActionControllerTest {

    private static final int FULL_DECK_SIZE = 74;
    private static final int SEE_THE_FUTURE_COUNT = 3;
    private final ActionController actionController = new ActionController();

    @Test
    void shuffleDeck_keepsDeckSize() {
        Deck deck = new Deck();
        actionController.shuffleDeck(deck);
        assertEquals(FULL_DECK_SIZE, deck.getSize());
    }

    @Test
    void shuffleDeck_verifyShuffleDeckCalled() {
        Deck deck = EasyMock.createMock(Deck.class);
        deck.shuffle();
        EasyMock.replay(deck);
        actionController.shuffleDeck(deck);
        EasyMock.verify(deck);
    }

    @Test
    void peekTopThree_fullDeck_returnsThreeCardsWithoutChangingDeck() {
        Deck deck = new Deck();
        List<Card> peeked = actionController.peekTopThree(deck);
        assertEquals(SEE_THE_FUTURE_COUNT, peeked.size());
        assertEquals(FULL_DECK_SIZE, deck.getSize());
    }

    @Test
    void peekTopThree_twoCardDeck_returnsTwoCards() {
        Deck deck = makeDeck(CardType.SKIP, CardType.ATTACK);
        assertEquals(2, actionController.peekTopThree(deck).size());
    }

    @Test
    void peekTopThree_emptyDeck_returnsEmptyList() {
        Deck deck = makeDeck();
        assertEquals(0, actionController.peekTopThree(deck).size());
    }

    @Test
    void reverseDirection_forward_becomesBackward() {
        TurnTracker turnTracker = new TurnTracker();
        actionController.reverseDirection(turnTracker);
        assertEquals(-1, turnTracker.getCurrentDirection());
    }

    @Test
    void reverseDirection_backward_becomesForward() {
        TurnTracker turnTracker = new TurnTracker();
        actionController.reverseDirection(turnTracker);
        actionController.reverseDirection(turnTracker);
        assertEquals(1, turnTracker.getCurrentDirection());
    }

    @Test
    void giveCard_movesChosenCardFromGiverToReceiver() {
        Player from = new Player(0);
        Player to = new Player(1);
        Card defuse = new Card(CardType.DEFUSE);
        from.addCardToHand(defuse);

        actionController.giveCard(from, to, 0);

        assertEquals(0, from.getHandSize());
        assertEquals(1, to.getHandSize());
        assertSame(defuse, to.getCardAt(0));
    }

    @Test
    void giveCard_emptyGiverHand_throwsIndexOutOfBoundsException() {
        Player from = new Player(0);
        Player to = new Player(1);
        assertThrows(
                IndexOutOfBoundsException.class,
                () -> {
                    actionController.giveCard(from, to, 0);
                });
    }

    @Test
    void stealRandomCard_singleCardVictim_movesThatCard() {
        Player from = new Player(0);
        Player to = new Player(1);
        Card attack = new Card(CardType.ATTACK);
        from.addCardToHand(attack);

        actionController.stealRandomCard(from, to);

        assertEquals(0, from.getHandSize());
        assertSame(attack, to.getCardAt(0));
    }

    @Test
    void stealRandomCard_manyCards_movesCardAtRandomIndex() {
        Player from = new Player(0);
        from.addCardToHand(new Card(CardType.SKIP));
        Card expected = new Card(CardType.NOPE);
        from.addCardToHand(expected);
        from.addCardToHand(new Card(CardType.FAVOR));
        Player to = new Player(1);
        ActionController seeded = new ActionController(new Random() {
            @Override
            public int nextInt(int bound) {
                return 1;
            }
        });

        seeded.stealRandomCard(from, to);

        assertSame(expected, to.getCardAt(0));
        assertEquals(2, from.getHandSize());
    }

    @Test
    void stealRandomCard_emptyVictim_isNoOp() {
        Player from = new Player(0);
        Player to = new Player(1);
        actionController.stealRandomCard(from, to);
        assertEquals(0, to.getHandSize());
    }

    @Test
    void stealDesiredCard_targetHasIt_movesThatCard() {
        Player from = new Player(0);
        from.addCardToHand(new Card(CardType.SKIP));
        Card desired = new Card(CardType.DEFUSE);
        from.addCardToHand(desired);
        Player to = new Player(1);

        actionController.stealDesiredCard(from, to, CardType.DEFUSE);

        assertSame(desired, to.getCardAt(0));
        assertEquals(1, from.getHandSize());
    }

    @Test
    void stealDesiredCard_targetLacksIt_isNoOp() {
        Player from = new Player(0);
        from.addCardToHand(new Card(CardType.SKIP));
        Player to = new Player(1);

        actionController.stealDesiredCard(from, to, CardType.DEFUSE);

        assertEquals(0, to.getHandSize());
        assertEquals(1, from.getHandSize());
    }

    private Deck makeDeck(CardType... types) {
        List<Card> cards = new ArrayList<>();
        for (CardType type : types) {
            cards.add(new Card(type));
        }
        return new Deck(cards);
    }
}
