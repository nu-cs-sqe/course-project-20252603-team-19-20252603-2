package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlayerTest {

    private static final int THREE_CARDS = 3;
    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player("Alice");
    }

    // Name
    @Test
    void testPlayerHasName() {
        assertEquals("Alice", player.getName());
    }

    // Alive by default
    @Test
    void testPlayerIsAliveByDefault() {
        assertTrue(player.isAlive());
    }

    // Hand starts empty
    @Test
    void testHandIsEmptyAtStart() {
        assertEquals(0, player.getHand().size());
    }

    // BVA: add 1 card
    @Test
    void testAddOneCard() {
        player.addCard(CardType.DEFUSE);
        assertEquals(1, player.getHand().size());
    }

    // BVA: add multiple cards
    @Test
    void testAddMultipleCards() {
        player.addCard(CardType.DEFUSE);
        player.addCard(CardType.ATTACK);
        player.addCard(CardType.NOPE);
        assertEquals(THREE_CARDS, player.getHand().size());
    }

    // BVA: add null card (invalid)
    @Test
    void testAddNullCardThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> player.addCard(null));
        assertEquals("Card cannot be null", exception.getMessage());
    }

    // BVA: remove a card that exists
    @Test
    void testRemoveCardThatExists() {
        player.addCard(CardType.DEFUSE);
        player.removeCard(CardType.DEFUSE);
        assertEquals(0, player.getHand().size());
    }

    // BVA: remove a card not in hand
    @Test
    void testRemoveCardNotInHandThrowsException() {
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> player.removeCard(CardType.ATTACK));
        assertEquals("Card not in hand: ATTACK", exception.getMessage());
    }

    // BVA: remove from empty hand
    @Test
    void testRemoveFromEmptyHandThrowsException() {
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> player.removeCard(CardType.DEFUSE));
        assertEquals("Card not in hand: DEFUSE", exception.getMessage());
    }

    // BVA: remove null card (invalid)
    @Test
    void testRemoveNullCardThrowsException() {
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> player.removeCard(null));
        assertEquals("Card not in hand: null", exception.getMessage());
    }

    // Explode sets player to dead
    @Test
    void testExplodeKillsPlayer() {
        player.explode();
        assertFalse(player.isAlive());
    }

    // BVA: exploding a dead player throws exception
    @Test
    void testExplodeAlreadyDeadPlayerThrowsException() {
        player.explode();
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> player.explode());
        assertEquals("Player is already dead", exception.getMessage());
    }

    // hasCard returns true if card in hand
    @Test
    void testHasCardReturnsTrueIfPresent() {
        player.addCard(CardType.DEFUSE);
        assertTrue(player.hasCard(CardType.DEFUSE));
    }

    // hasCard returns false if card not in hand
    @Test
    void testHasCardReturnsFalseIfAbsent() {
        assertFalse(player.hasCard(CardType.DEFUSE));
    }

    // BVA: getHand is read-only
    @Test
    void testGetHandReturnsUnmodifiableView() {
        player.addCard(CardType.DEFUSE);
        assertThrows(
                UnsupportedOperationException.class,
                () -> player.getHand().add(CardType.ATTACK));
    }
}
