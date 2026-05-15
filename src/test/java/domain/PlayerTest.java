package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
}
