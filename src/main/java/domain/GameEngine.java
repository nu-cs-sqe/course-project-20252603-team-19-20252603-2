package domain;

public final class GameEngine {

    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 5;

    private static final String NUM_PLAYERS_OUT_OF_RANGE_KEY = "gameEngine.numPlayers.outOfRange";

    private final int numPlayers;

    public GameEngine(int numPlayers) {
        if (numPlayers < MIN_PLAYERS || numPlayers > MAX_PLAYERS) {
            throw new IllegalArgumentException(NUM_PLAYERS_OUT_OF_RANGE_KEY);
        }
        this.numPlayers = numPlayers;
    }

    public int getNumPlayers() {
        return numPlayers;
    }
}
