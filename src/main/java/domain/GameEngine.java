package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class GameEngine {

    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 5;
    private static final int INITIAL_NON_DEFUSE_CARDS_PER_PLAYER = 4;
    private static final int TOTAL_DEFUSES = 6;

    private static final String NUM_PLAYERS_OUT_OF_RANGE_KEY = "gameEngine.numPlayers.outOfRange";
    private static final String INVALID_PLAYER_ID_KEY = "gameEngine.getPlayer.invalidId";

    private final int numPlayers;
    private final List<Player> players;
    private final TurnTracker turnTracker;
    private final Deck deck;

    public GameEngine(int numPlayers) {
        if (numPlayers < MIN_PLAYERS || numPlayers > MAX_PLAYERS) {
            throw new IllegalArgumentException(NUM_PLAYERS_OUT_OF_RANGE_KEY);
        }
        this.numPlayers = numPlayers;
        this.players = new ArrayList<>();
        for (int id = 0; id < numPlayers; id++) {
            this.players.add(new Player(id));
        }
        this.turnTracker = new TurnTracker();
        this.turnTracker.setNumTotalPlayers(numPlayers);

        List<Card> nonSpecialPool = buildShuffledNonSpecialPool();
        dealStartingHands(this.players, nonSpecialPool);
        this.deck = buildRiggedDeck(numPlayers, nonSpecialPool);
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

    public int getCurrentPlayerId() {
        return turnTracker.getCurrentPlayer();
    }

    public int getDrawPileSize() {
        return deck.getSize();
    }

    public boolean isDeckEmpty() {
        return deck.isEmpty();
    }

    public List<Card> getPlayerHand(int playerId) {
        return getPlayer(playerId).getHand();
    }

    private static List<Card> buildShuffledNonSpecialPool() {
        List<Card> pool = new ArrayList<>();
        for (Card card : new Deck().getDrawPile()) {
            CardType type = card.getCardType();
            if (type != CardType.EXPLODING_KITTEN && type != CardType.DEFUSE) {
                pool.add(card);
            }
        }
        Collections.shuffle(pool);
        return pool;
    }

    private static void dealStartingHands(List<Player> players, List<Card> nonSpecialPool) {
        for (Player player : players) {
            for (int i = 0; i < INITIAL_NON_DEFUSE_CARDS_PER_PLAYER; i++) {
                player.addCardToHand(nonSpecialPool.remove(0));
            }
            player.addCardToHand(new Card(CardType.DEFUSE));
        }
    }

    private static Deck buildRiggedDeck(int numPlayers, List<Card> remainingNonSpecial) {
        List<Card> drawPile = new ArrayList<>(remainingNonSpecial);
        int defusesLeft = TOTAL_DEFUSES - numPlayers;
        for (int i = 0; i < defusesLeft; i++) {
            drawPile.add(new Card(CardType.DEFUSE));
        }
        int kittensInDeck = numPlayers - 1;
        for (int i = 0; i < kittensInDeck; i++) {
            drawPile.add(new Card(CardType.EXPLODING_KITTEN));
        }
        Collections.shuffle(drawPile);
        return new Deck(drawPile);
    }
}
