package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TurnTrackerTest {

    @Test
    public void turnGoesToNextPlayer_player0_player1() {
        TurnTracker turnTracker = new TurnTracker();
        final int numTotalPlayers = 3;
        final int currentPlayer = 0;
        final int currentDirection = 1;
        turnTracker.setNumTotalPlayers(numTotalPlayers);
        turnTracker.setCurrentPlayer(currentPlayer);
        turnTracker.setCurrentDirection(currentDirection);

        turnTracker.turnGoesToNextPlayer();

        final int expectedPlayer = 1;
        final int actualPlayer = turnTracker.getCurrentPlayer();
        assertEquals(expectedPlayer, actualPlayer);
    }


    @Test
    public void turnGoesToNextPlayer_player2_player0() {
        TurnTracker turnTracker = new TurnTracker();
        final int numTotalPlayers = 3;
        final int currentPlayer = 2;
        final int currentDirection = 1;
        turnTracker.setNumTotalPlayers(numTotalPlayers);
        turnTracker.setCurrentPlayer(currentPlayer);
        turnTracker.setCurrentDirection(currentDirection);

        turnTracker.turnGoesToNextPlayer();

        final int expectedPlayer = 0;
        final int actualPlayer = turnTracker.getCurrentPlayer();
        assertEquals(expectedPlayer, actualPlayer);
    }

}
