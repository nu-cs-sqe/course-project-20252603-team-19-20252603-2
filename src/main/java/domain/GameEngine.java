package domain;

import java.util.ArrayList;
import java.util.List;

public final class GameEngine {

    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 5;

    private static final String NUM_PLAYERS_OUT_OF_RANGE_KEY = "gameEngine.numPlayers.outOfRange";
    private static final String INVALID_PLAYER_ID_KEY = "gameEngine.getPlayer.invalidId";

    private final int numPlayers;
    private final List<Player> players;

    public GameEngine(int numPlayers) {
        if (numPlayers < MIN_PLAYERS || numPlayers > MAX_PLAYERS) {
            throw new IllegalArgumentException(NUM_PLAYERS_OUT_OF_RANGE_KEY);
        }
        this.numPlayers = numPlayers;
        this.players = new ArrayList<>();
        for (int id = 0; id < numPlayers; id++) {
            this.players.add(new Player(id));
        }
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public Player getPlayer(int playerId) {
        if (playerId < 0 || playerId >= numPlayers) {
            throw new IllegalArgumentException(INVALID_PLAYER_ID_KEY);
        }
        return players.get(playerId);
    }
}
