package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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


    @ParameterizedTest
    @CsvSource({
            "0, 1, 1", "1, 1, 2", "2, 1, 3", "3, 1, 4", "4, 1, 5",
            "5, 1, 6", "6, 1, 7", "7, 1, 8", "8, 1, 9", "9, 1, 0",
            "0, -1, 9", "1, -1, 0", "2, -1, 1", "3, -1, 2", "4, -1, 3",
            "5, -1, 4", "6, -1, 5", "7, -1, 6", "8, -1, 7", "9, -1, 8",
    })
    public void turnGoesToNextPlayer_10TotalPlayers_shouldAdvanceToNextPlayer(
            int currentPlayer,
            int currentDirection,
            int nextPlayer) {
        TurnTracker turnTracker = new TurnTracker();
        final int numTotalPlayers = 10;
        turnTracker.setNumTotalPlayers(numTotalPlayers);
        turnTracker.setCurrentPlayer(currentPlayer);
        turnTracker.setCurrentDirection(currentDirection);

        turnTracker.turnGoesToNextPlayer();

        final int expectedPlayer = nextPlayer;
        final int actualPlayer = turnTracker.getCurrentPlayer();
        assertEquals(expectedPlayer, actualPlayer);
    }


    @ParameterizedTest
    @CsvSource({
            "0, 1, 1", "1, 1, 0",
            "0, -1, 1", "1, -1, 0",
    })
    public void turnGoesToNextPlayer_2TotalPlayers_shouldAdvanceToNextPlayer(
            int currentPlayer,
            int currentDirection,
            int nextPlayer) {
        TurnTracker turnTracker = new TurnTracker();
        final int numTotalPlayers = 2;
        turnTracker.setNumTotalPlayers(numTotalPlayers);
        turnTracker.setCurrentPlayer(currentPlayer);
        turnTracker.setCurrentDirection(currentDirection);

        turnTracker.turnGoesToNextPlayer();

        final int expectedPlayer = nextPlayer;
        final int actualPlayer = turnTracker.getCurrentPlayer();
        assertEquals(expectedPlayer, actualPlayer);
    }


    @ParameterizedTest
    @CsvSource({
            "0, 1, 1", "1, 1, 2", "2, 1, 0",
            "0, -1, 2", "1, -1, 0", "2, -1, 1"
    })
    public void turnGoesToNextPlayer_3TotalPlayers_shouldAdvanceToNextPlayer(
            int currentPlayer,
            int currentDirection,
            int nextPlayer) {
        TurnTracker turnTracker = new TurnTracker();
        final int numTotalPlayers = 3;
        turnTracker.setNumTotalPlayers(numTotalPlayers);
        turnTracker.setCurrentPlayer(currentPlayer);
        turnTracker.setCurrentDirection(currentDirection);

        turnTracker.turnGoesToNextPlayer();

        final int expectedPlayer = nextPlayer;
        final int actualPlayer = turnTracker.getCurrentPlayer();
        assertEquals(expectedPlayer, actualPlayer);
    }


    @ParameterizedTest
    @CsvSource({
            "0, 1, 0", "1, 1, 1",
            "0, -1, 0", "1, -1, 1",
    })
    public void turnSkipsNextPlayer_2TotalPlayers_shouldSkipNextPlayer(
            int currentPlayer,
            int currentDirection,
            int nextPlayer) {
        TurnTracker turnTracker = new TurnTracker();
        final int numTotalPlayers = 2;
        turnTracker.setNumTotalPlayers(numTotalPlayers);
        turnTracker.setCurrentPlayer(currentPlayer);
        turnTracker.setCurrentDirection(currentDirection);

        turnTracker.turnSkipsNextPlayer();

        final int expectedPlayer = nextPlayer;
        final int actualPlayer = turnTracker.getCurrentPlayer();
        assertEquals(expectedPlayer, actualPlayer);
    }


    @ParameterizedTest
    @CsvSource({
            "0, 1, 2", "1, 1, 0", "2, 1, 1",
            "0, -1, 1", "1, -1, 2", "2, -1, 0"
    })
    public void turnSkipsNextPlayer_3TotalPlayers_shouldSkipNextPlayer(
            int currentPlayer,
            int currentDirection,
            int nextPlayer) {
        TurnTracker turnTracker = new TurnTracker();
        final int numTotalPlayers = 3;
        turnTracker.setNumTotalPlayers(numTotalPlayers);
        turnTracker.setCurrentPlayer(currentPlayer);
        turnTracker.setCurrentDirection(currentDirection);

        turnTracker.turnSkipsNextPlayer();

        final int expectedPlayer = nextPlayer;
        final int actualPlayer = turnTracker.getCurrentPlayer();
        assertEquals(expectedPlayer, actualPlayer);
    }


    @ParameterizedTest
    @CsvSource({
            "0, 1, 2", "1, 1, 3", "2, 1, 4", "3, 1, 5", "4, 1, 6",
            "5, 1, 7", "6, 1, 8", "7, 1, 9", "8, 1, 0", "9, 1, 1",
            "0, -1, 8", "1, -1, 9", "2, -1, 0", "3, -1, 1", "4, -1, 2",
            "5, -1, 3", "6, -1, 4", "7, -1, 5", "8, -1, 6", "9, -1, 7",
    })
    public void turnSkipsNextPlayer_10TotalPlayers_shouldSkipNextPlayer(
            int currentPlayer,
            int currentDirection,
            int nextPlayer) {
        TurnTracker turnTracker = new TurnTracker();
        final int numTotalPlayers = 10;
        turnTracker.setNumTotalPlayers(numTotalPlayers);
        turnTracker.setCurrentPlayer(currentPlayer);
        turnTracker.setCurrentDirection(currentDirection);

        turnTracker.turnSkipsNextPlayer();

        final int expectedPlayer = nextPlayer;
        final int actualPlayer = turnTracker.getCurrentPlayer();
        assertEquals(expectedPlayer, actualPlayer);
    }



    @Test
    public void changeCurrentDirection_3TotalPlayers_shouldChangeDirection() {
        TurnTracker turnTracker = new TurnTracker();
        final int numTotalPlayers = 3;
        final int currentPlayer = 0;
        final int currentDirection = 1;
        turnTracker.setNumTotalPlayers(numTotalPlayers);
        turnTracker.setCurrentPlayer(currentPlayer);
        turnTracker.setCurrentDirection(currentDirection);

        turnTracker.changeCurrentDirection();

        final int expectedPlayer = 0;
        final int actualPlayer = turnTracker.getCurrentPlayer();
        assertEquals(expectedPlayer, actualPlayer);

        final int expectedDirection = -1;
        final int actualDirection = turnTracker.getCurrentDirection();
        assertEquals(expectedDirection, actualDirection);
    }


    @Test
    public void changeCurrentDirection_4TotalPlayers_shouldChangeDirection() {
        TurnTracker turnTracker = new TurnTracker();
        final int numTotalPlayers = 4;
        final int currentPlayer = 0;
        final int currentDirection = -1;
        turnTracker.setNumTotalPlayers(numTotalPlayers);
        turnTracker.setCurrentPlayer(currentPlayer);
        turnTracker.setCurrentDirection(currentDirection);

        turnTracker.changeCurrentDirection();

        final int expectedPlayer = 0;
        final int actualPlayer = turnTracker.getCurrentPlayer();
        assertEquals(expectedPlayer, actualPlayer);

        final int expectedDirection = 1;
        final int actualDirection = turnTracker.getCurrentDirection();
        assertEquals(expectedDirection, actualDirection);
    }


    @Test
    public void setNumTotalPlayers_belowMinimum_throwsIllegalArgumentException() {
        TurnTracker turnTracker = new TurnTracker();
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> turnTracker.setNumTotalPlayers(1));
        assertEquals("turnTracker.numPlayers.tooSmall", exception.getMessage());
    }


    @Test
    public void setNumTotalPlayers_minimum_setsValue() {
        TurnTracker turnTracker = new TurnTracker();
        turnTracker.setNumTotalPlayers(2);
        assertEquals(2, turnTracker.getNumTotalPlayers());
    }


    @Test
    public void setCurrentDirection_invalid_throwsIllegalArgumentException() {
        TurnTracker turnTracker = new TurnTracker();
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> turnTracker.setCurrentDirection(0));
        assertEquals("turnTracker.currentDirection.invalid", exception.getMessage());
    }


    @Test
    public void setCurrentDirection_forward_setsValue() {
        TurnTracker turnTracker = new TurnTracker();
        turnTracker.setCurrentDirection(1);
        assertEquals(1, turnTracker.getCurrentDirection());
    }


    @Test
    public void setCurrentDirection_backward_setsValue() {
        TurnTracker turnTracker = new TurnTracker();
        turnTracker.setCurrentDirection(-1);
        assertEquals(-1, turnTracker.getCurrentDirection());
    }


}