package domain;

public class TurnTracker {
    private int numTotalPlayers = 1;
    private int currentPlayer = 0;
    private int currentDirection = 1;

    private static final String NUM_TOTAL_PLAYERS_LESS_THAN_TWO = "turnTracker.numPlayers.tooSmall";

    int getNumTotalPlayers() {
        return this.numTotalPlayers;
    }

    void setNumTotalPlayers(int numTotalPlayers) {
        if (numTotalPlayers < 2) {
            throw new IllegalArgumentException(NUM_TOTAL_PLAYERS_LESS_THAN_TWO);
        } else {
            this.numTotalPlayers = numTotalPlayers;
        }
    }

    int getCurrentPlayer() {
        return this.currentPlayer;
    }

    void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    int getCurrentDirection() {
        return this.currentDirection;
    }

    void setCurrentDirection(int currentDirection) {
        this.currentDirection = currentDirection;
    }

    public void turnGoesToNextPlayer() {
        setCurrentPlayer((getCurrentPlayer()
                            + getCurrentDirection()
                            + getNumTotalPlayers())
                        % getNumTotalPlayers());
    }



}
