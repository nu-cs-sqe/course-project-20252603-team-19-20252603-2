package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

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
        assertEquals(3, player.getHand().size());
    }

    // BVA: add null card (invalid)
    @Test
    void testAddNullCardThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> player.addCard(null));
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
        assertThrows(IllegalArgumentException.class, () -> player.removeCard(CardType.ATTACK));
    }

    // BVA: remove from empty hand
    @Test
    void testRemoveFromEmptyHandThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> player.removeCard(CardType.DEFUSE));
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
        assertThrows(IllegalStateException.class, () -> player.explode());
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
}