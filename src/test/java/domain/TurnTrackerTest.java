package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TurnTrackerTest {

    @Test
    public void turnGoesToNextPlayer_player0_player1() {
        TurnTracker turnTracker = new TurnTracker();
        turnTracker.turnGoesToNextPlayer();

        int expectedPlayer = 1;
        int actualPlayer = turnTracker.getCurrentPlayer();
        assertEquals(expectedPlayer, actualPlayer);
    }

}
