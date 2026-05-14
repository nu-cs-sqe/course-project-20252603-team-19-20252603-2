package domain;

public class TurnTracker {
    private int numTotalPlayers = 0;
    private int currentPlayer = 0;
    private int currentDirection = 1;

    int getNumTotalPlayers() {
        return this.numTotalPlayers;
    }

    void setNumTotalPlayers(int numTotalPlayers) {
        this.numTotalPlayers = numTotalPlayers;
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
        setCurrentPlayer(1);
    }



}
