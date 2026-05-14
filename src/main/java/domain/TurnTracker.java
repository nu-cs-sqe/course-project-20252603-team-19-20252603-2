package domain;

public class TurnTracker {
    private int numTotalPlayers = 1;
    private static final int MIN_TOTAL_PLAYERS = 2;
    private int currentPlayer = 0;
    private int currentDirection = 1;

    private static final String NUM_TOTAL_PLAYERS_LESS_THAN_TWO = "turnTracker.numPlayers.tooSmall";

    public int getNumTotalPlayers() {
        return this.numTotalPlayers;
    }

    public void setNumTotalPlayers(int numTotalPlayers) {
        if (numTotalPlayers < MIN_TOTAL_PLAYERS) {
            throw new IllegalArgumentException(NUM_TOTAL_PLAYERS_LESS_THAN_TWO);
        } else {
            this.numTotalPlayers = numTotalPlayers;
        }
    }

    public int getCurrentPlayer() {
        return this.currentPlayer;
    }

    void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public int getCurrentDirection() {
        return this.currentDirection;
    }

    void setCurrentDirection(int currentDirection) {
        this.currentDirection = currentDirection;
    }

    public void changeCurrentDirection() {
        this.currentDirection *= -1;
    }

    public void turnGoesToNextPlayer() {
        setCurrentPlayer((getCurrentPlayer()
                            + getCurrentDirection()
                            + getNumTotalPlayers())
                        % getNumTotalPlayers());
    }

    public void turnSkipsNextPlayer() {
        setCurrentPlayer((getCurrentPlayer()
                + getCurrentDirection() * 2
                + getNumTotalPlayers())
                % getNumTotalPlayers());
    }



}
