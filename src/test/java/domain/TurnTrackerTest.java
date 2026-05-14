package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

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
