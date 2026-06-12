package domain;

public class TurnTracker {
    private int numTotalPlayers = 2;
    private static final int MIN_TOTAL_PLAYERS = 2;
    private int currentPlayer = 0;
    private int currentDirection = 1;

    private static final String NUM_TOTAL_PLAYERS_LESS_THAN_TWO = "turnTracker.numPlayers.tooSmall";
    private static final String INVALID_PLAYING_DIRECTION = "turnTracker.currentDirection.invalid";

    public int getNumTotalPlayers() {
        return this.numTotalPlayers;
    }

    public void setNumTotalPlayers(int numTotalPlayers) {
        if (numTotalPlayers < MIN_TOTAL_PLAYERS) {
            throw new IllegalArgumentException(NUM_TOTAL_PLAYERS_LESS_THAN_TWO);
        }
        this.numTotalPlayers = numTotalPlayers;
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
        if (currentDirection != 1 && currentDirection != -1) {
            throw new IllegalArgumentException(INVALID_PLAYING_DIRECTION);
        }
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