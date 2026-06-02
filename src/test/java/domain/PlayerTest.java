package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class PlayerTest {

    @Test
    void getPlayerId_returnsIdPassedAtConstruction_zero() {
        Player player = new Player(0);
        assertEquals(0, player.getPlayerId());
    }

    @Test
    void getPlayerId_returnsIdPassedAtConstruction_one() {
        Player player = new Player(1);
        assertEquals(1, player.getPlayerId());
    }

    @Test
    void newPlayer_handIsEmpty() {
        assertEquals(0, new Player(0).getHandSize());
    }

    @Test
    void addCardToHand_increasesHandSize_byOne() {
        Player player = new Player(0);
        player.addCardToHand(new Card(CardType.DEFUSE));
        assertEquals(1, player.getHandSize());
    }

    @Test
    void addCardToHand_increasesHandSize_byTwo() {
        Player player = new Player(0);
        player.addCardToHand(new Card(CardType.DEFUSE));
        player.addCardToHand(new Card(CardType.ATTACK));
        assertEquals(2, player.getHandSize());
    }

    @Test
    void addCardToHand_null_throwsIllegalArgumentException() {
        Player player = new Player(0);
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> player.addCardToHand(null));
        assertEquals("player.addCardToHand.nullCard", ex.getMessage());
    }

    @Test
    void getCardAt_returnsCardAtIndex() {
        Player player = new Player(0);
        Card defuse = new Card(CardType.DEFUSE);
        player.addCardToHand(defuse);
        assertSame(defuse, player.getCardAt(0));
    }

    @Test
    void getCardAt_negativeIndex_throwsIndexOutOfBoundsException() {
        Player player = new Player(0);
        player.addCardToHand(new Card(CardType.DEFUSE));
        IndexOutOfBoundsException ex = assertThrows(
                IndexOutOfBoundsException.class,
                () -> {
                    player.getCardAt(-1);
                });
        assertEquals("player.getCardAt.invalidIndex", ex.getMessage());
    }

    @Test
    void getCardAt_indexEqualToSize_throwsIndexOutOfBoundsException() {
        Player player = new Player(0);
        player.addCardToHand(new Card(CardType.DEFUSE));
        IndexOutOfBoundsException ex = assertThrows(
                IndexOutOfBoundsException.class,
                () -> {
                    player.getCardAt(1);
                });
        assertEquals("player.getCardAt.invalidIndex", ex.getMessage());
    }

    @Test
    void getCardAt_emptyHand_throwsIndexOutOfBoundsException() {
        Player player = new Player(0);
        IndexOutOfBoundsException ex = assertThrows(
                IndexOutOfBoundsException.class,
                () -> {
                    player.getCardAt(0);
                });
        assertEquals("player.getCardAt.invalidIndex", ex.getMessage());
    }

    @Test
    void removeCardFromHand_returnsRemovedCard() {
        Player player = new Player(0);
        Card defuse = new Card(CardType.DEFUSE);
        player.addCardToHand(defuse);
        assertSame(defuse, player.removeCardFromHand(0));
    }

    @Test
    void removeCardFromHand_decrementsHandSize() {
        Player player = new Player(0);
        player.addCardToHand(new Card(CardType.DEFUSE));
        player.removeCardFromHand(0);
        assertEquals(0, player.getHandSize());
    }

    @Test
    void removeCardFromHand_negativeIndex_throwsIndexOutOfBoundsException() {
        Player player = new Player(0);
        player.addCardToHand(new Card(CardType.DEFUSE));
        IndexOutOfBoundsException ex = assertThrows(
                IndexOutOfBoundsException.class,
                () -> {
                    player.removeCardFromHand(-1);
                });
        assertEquals("player.removeCardFromHand.invalidIndex", ex.getMessage());
    }

    @Test
    void removeCardFromHand_emptyHand_throwsIndexOutOfBoundsException() {
        Player player = new Player(0);
        IndexOutOfBoundsException ex = assertThrows(
                IndexOutOfBoundsException.class,
                () -> {
                    player.removeCardFromHand(0);
                });
        assertEquals("player.removeCardFromHand.invalidIndex", ex.getMessage());
    }

    @Test
    void hasCard_returnsTrue_whenCardOfTypeIsPresent() {
        Player player = new Player(0);
        player.addCardToHand(new Card(CardType.DEFUSE));
        assertTrue(player.hasCard(CardType.DEFUSE));
    }

    @Test
    void hasCard_returnsFalse_whenCardOfTypeIsAbsent() {
        Player player = new Player(0);
        player.addCardToHand(new Card(CardType.DEFUSE));
        assertFalse(player.hasCard(CardType.ATTACK));
    }

    @Test
    void hasCard_returnsFalse_onEmptyHand() {
        Player player = new Player(0);
        assertFalse(player.hasCard(CardType.DEFUSE));
    }

    @Test
    void getIndexOfCard_returnsIndex_whenCardOfTypeIsPresent() {
        Player player = new Player(0);
        player.addCardToHand(new Card(CardType.DEFUSE));
        player.addCardToHand(new Card(CardType.ATTACK));
        assertEquals(1, player.getIndexOfCard(CardType.ATTACK));
    }

    @Test
    void getIndexOfCard_returnsNegativeOne_whenCardOfTypeIsAbsent() {
        Player player = new Player(0);
        player.addCardToHand(new Card(CardType.DEFUSE));
        assertEquals(-1, player.getIndexOfCard(CardType.ATTACK));
    }

    @Test
    void getIndexOfCard_returnsFirstIndex_whenDuplicatesPresent() {
        Player player = new Player(0);
        player.addCardToHand(new Card(CardType.DEFUSE));
        player.addCardToHand(new Card(CardType.DEFUSE));
        assertEquals(0, player.getIndexOfCard(CardType.DEFUSE));
    }

    @Test
    void getIndexOfCard_returnsNegativeOne_onEmptyHand() {
        Player player = new Player(0);
        assertEquals(-1, player.getIndexOfCard(CardType.DEFUSE));
    }

    @Test
    void isAlive_newPlayer_returnsTrue() {
        assertTrue(new Player(0).isAlive());
    }

    @Test
    void isAlive_afterMarkDead_returnsFalse() {
        Player player = new Player(0);
        player.markDead();
        assertFalse(player.isAlive());
    }

    @Test
    void markDead_alreadyDead_throwsIllegalStateException() {
        Player player = new Player(0);
        player.markDead();
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> player.markDead());
        assertEquals("player.markDead.alreadyDead", ex.getMessage());
    }

    @Test
    void getHand_newPlayer_returnsEmptyList() {
        Player player = new Player(0);
        assertEquals(0, player.getHand().size());
    }

    @Test
    void getHand_afterAddCard_containsThatCard() {
        Player player = new Player(0);
        Card defuse = new Card(CardType.DEFUSE);
        player.addCardToHand(defuse);
        List<Card> hand = player.getHand();
        assertEquals(1, hand.size());
        assertSame(defuse, hand.get(0));
    }

    @Test
    void getHand_returnedListIsDefensiveCopy() {
        Player player = new Player(0);
        player.addCardToHand(new Card(CardType.DEFUSE));
        player.getHand().add(new Card(CardType.ATTACK));
        assertEquals(1, player.getHandSize());
    }
}
