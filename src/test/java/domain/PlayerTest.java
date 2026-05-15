package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
}
