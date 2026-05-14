package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TurnTrackerTest {

    @Test
    public void turnGoesToNextPlayer_player0_player1() {
        TurnTracker turnTracker = new TurnTracker();
        final int numTotalPlayers = 3;
        turnTracker.setNumTotalPlayers(numTotalPlayers);
        turnTracker.setCurrentDirection(1);

        turnTracker.turnGoesToNextPlayer();

        int expectedPlayer = 1;
        int actualPlayer = turnTracker.getCurrentPlayer();
        assertEquals(expectedPlayer, actualPlayer);
    }

}
