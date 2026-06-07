package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class GameEngine {

    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 5;
    private static final int INITIAL_NON_DEFUSE_CARDS_PER_PLAYER = 4;
    private static final int TOTAL_DEFUSES = 6;
    private static final int NORMAL_FORCED_TURNS = 1;
    private static final int TURNS_ADDED_BY_ATTACK = 2;

    private static final String NUM_PLAYERS_OUT_OF_RANGE_KEY = "gameEngine.numPlayers.outOfRange";
    private static final String INVALID_PLAYER_ID_KEY = "gameEngine.getPlayer.invalidId";
    private static final String NOT_IN_HAND_KEY = "gameEngine.play.notInHand";
    private static final String NO_KITTEN_KEY = "gameEngine.defuse.noKitten";
    private static final String NO_DEFUSE_KEY = "gameEngine.defuse.noDefuse";
    private static final String NOT_OVER_KEY = "gameEngine.notOver";

    private final int numPlayers;
    private final List<Player> players;
    private final TurnTracker turnTracker;
    private final Deck deck;
    private final RuleManager ruleManager;
    private final ActionController actionController;

    private int forcedTurns = NORMAL_FORCED_TURNS;
    private CardType lastPlayedCard;

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
        this.ruleManager = new RuleManager();
        this.actionController = new ActionController();

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

    public Card drawCardForCurrentPlayer() {
        Card drawn = deck.drawTop();
        getPlayer(getCurrentPlayerId()).addCardToHand(drawn);
        return drawn;
    }

    public void advanceToNextPlayer() {
        advanceToNextLivingPlayer();
    }

    public void endTurnByDrawing() {
        consumeOneForcedTurn();
    }

    public List<Card> getDiscardPile() {
        return deck.getDiscardPile();
    }

    public int getForcedTurns() {
        return forcedTurns;
    }

    public CardType getLastPlayedCard() {
        return lastPlayedCard;
    }

    public void playSkip() {
        playFromHand(CardType.SKIP);
        consumeOneForcedTurn();
    }

    public void playShuffle() {
        playFromHand(CardType.SHUFFLE);
        actionController.shuffleDeck(deck);
    }

    public List<Card> playSeeTheFuture() {
        playFromHand(CardType.SEE_THE_FUTURE);
        return actionController.peekTopThree(deck);
    }

    public void playReverse() {
        playFromHand(CardType.REVERSE);
        actionController.reverseDirection(turnTracker);
        consumeOneForcedTurn();
    }

    public void playAttack() {
        playFromHand(CardType.ATTACK);
        int transferred = forcedTurns == NORMAL_FORCED_TURNS ? 0 : forcedTurns;
        advanceToNextLivingPlayer();
        forcedTurns = transferred + TURNS_ADDED_BY_ATTACK;
    }

    public void playTargetedAttack(int targetId) {
        Player current = getPlayer(getCurrentPlayerId());
        Player target = getPlayer(targetId);
        ruleManager.requireValidTarget(current, target);
        playFromHand(CardType.TARGETED_ATTACK);
        int transferred = forcedTurns == NORMAL_FORCED_TURNS ? 0 : forcedTurns;
        turnTracker.setCurrentPlayer(targetId);
        forcedTurns = transferred + TURNS_ADDED_BY_ATTACK;
    }

    public void playFavor(int targetId, int cardIndex) {
        Player current = getPlayer(getCurrentPlayerId());
        Player target = getPlayer(targetId);
        ruleManager.requireValidTarget(current, target);
        playFromHand(CardType.FAVOR);
        actionController.giveCard(target, current, cardIndex);
    }

    public void playCatPair(int targetId, CardType cardType) {
        Player current = getPlayer(getCurrentPlayerId());
        Player target = getPlayer(targetId);
        ruleManager.requireValidTarget(current, target);
        ruleManager.requireCatPair(current, cardType);
        discardOneFromCurrent(cardType);
        discardOneFromCurrent(cardType);
        lastPlayedCard = cardType;
        actionController.stealRandomCard(target, current);
    }

    public void defuseDrawnKitten(int reinsertIndex) {
        Player current = getPlayer(getCurrentPlayerId());
        int kittenIndex = current.getIndexOfCard(CardType.EXPLODING_KITTEN);
        if (kittenIndex < 0) {
            throw new IllegalStateException(NO_KITTEN_KEY);
        }
        if (current.getIndexOfCard(CardType.DEFUSE) < 0) {
            throw new IllegalStateException(NO_DEFUSE_KEY);
        }
        Card kitten = current.removeCardFromHand(kittenIndex);
        deck.discard(current.removeCardFromHand(current.getIndexOfCard(CardType.DEFUSE)));
        deck.insertAt(kitten, reinsertIndex);
        consumeOneForcedTurn();
    }

    public void explodeCurrentPlayer() {
        Player current = getPlayer(getCurrentPlayerId());
        int kittenIndex = current.getIndexOfCard(CardType.EXPLODING_KITTEN);
        if (kittenIndex < 0) {
            throw new IllegalStateException(NO_KITTEN_KEY);
        }
        deck.discard(current.removeCardFromHand(kittenIndex));
        current.markDead();
        while (current.getHandSize() > 0) {
            deck.discard(current.removeCardFromHand(0));
        }
        advanceToNextLivingPlayer();
        forcedTurns = NORMAL_FORCED_TURNS;
    }

    public void playNope(int noperId) {
        ruleManager.requireSomethingToNope(lastPlayedCard);
        Player noper = getPlayer(noperId);
        int index = noper.getIndexOfCard(CardType.NOPE);
        if (index < 0) {
            throw new IllegalStateException(NOT_IN_HAND_KEY);
        }
        deck.discard(noper.removeCardFromHand(index));
        lastPlayedCard = null;
    }

    public boolean isGameOver() {
        return countAlive() == 1 || deck.isEmpty();
    }

    public int getWinnerId() {
        if (!isGameOver()) {
            throw new IllegalStateException(NOT_OVER_KEY);
        }
        int winnerId = -1;
        int mostCards = -1;
        for (Player player : players) {
            if (player.isAlive() && player.getHandSize() > mostCards) {
                mostCards = player.getHandSize();
                winnerId = player.getPlayerId();
            }
        }
        return winnerId;
    }

    private int countAlive() {
        int alive = 0;
        for (Player player : players) {
            if (player.isAlive()) {
                alive++;
            }
        }
        return alive;
    }

    private void discardOneFromCurrent(CardType type) {
        Player current = getPlayer(getCurrentPlayerId());
        deck.discard(current.removeCardFromHand(current.getIndexOfCard(type)));
    }

    private void playFromHand(CardType type) {
        ruleManager.requirePlayable(type);
        Player current = getPlayer(getCurrentPlayerId());
        int index = current.getIndexOfCard(type);
        if (index < 0) {
            throw new IllegalStateException(NOT_IN_HAND_KEY);
        }
        deck.discard(current.removeCardFromHand(index));
        lastPlayedCard = type;
    }

    private void consumeOneForcedTurn() {
        forcedTurns--;
        if (forcedTurns <= 0) {
            advanceToNextLivingPlayer();
            forcedTurns = NORMAL_FORCED_TURNS;
        }
    }

    private void advanceToNextLivingPlayer() {
        do {
            turnTracker.turnGoesToNextPlayer();
        } while (!getPlayer(getCurrentPlayerId()).isAlive());
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
