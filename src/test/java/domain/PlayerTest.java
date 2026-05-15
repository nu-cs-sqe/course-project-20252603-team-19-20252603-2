package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
