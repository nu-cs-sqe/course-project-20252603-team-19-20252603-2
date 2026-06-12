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
    private static final int TURNS_ADDED_PERSONAL_ATTACK = 3;

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
    private int lastPlayerId;

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

    public List<Card> getDrawPile() {
        return deck.getDrawPile();
    }

    public int getForcedTurns() {
        return forcedTurns;
    }

    public CardType getLastPlayedCard() {
        return lastPlayedCard;
    }

    public int getCurrentDirection() {
        return turnTracker.getCurrentDirection();
    }

    public void setLastPlayedCard(CardType cardType) {
        lastPlayedCard = cardType;
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
        lastPlayedCard = CardType.SEE_THE_FUTURE;
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
        lastPlayedCard = CardType.TARGETED_ATTACK;
    }

    public void playFavor(int targetId, int cardIndex) {
        Player current = getPlayer(getCurrentPlayerId());
        Player target = getPlayer(targetId);
        ruleManager.requireValidTarget(current, target);
        playFromHand(CardType.FAVOR);
        actionController.giveCard(target, current, cardIndex);
    }

    public void playCatPair(int targetId, List<CardType> selectedCards) {
        Player current = getPlayer(getCurrentPlayerId());
        Player target = getPlayer(targetId);

        CardType selectedCard = selectedCards.contains(CardType.CAT_CARDS)
                ? CardType.CAT_CARDS
                : selectedCards.get(0);

        ruleManager.requireValidTarget(current, target);
        ruleManager.requireCatPair(current, selectedCard);

        for (CardType card : selectedCards) {
            discardOneFromCurrent(card);
        }

        recordPlay(selectedCard);
        actionController.stealRandomCard(target, current);
    }

    public void playCatTriple(int targetId, List<CardType> selectedCards, CardType desiredCard) {
        Player current = getPlayer(getCurrentPlayerId());
        Player target = getPlayer(targetId);

        CardType selectedCard = selectedCards.contains(CardType.CAT_CARDS)
                ? CardType.CAT_CARDS
                : selectedCards.get(0);

        ruleManager.requireValidTarget(current, target);
        ruleManager.requireCatTriple(current, selectedCard);

        for (CardType card : selectedCards) {
            discardOneFromCurrent(card);
        }

        recordPlay(selectedCard);
        actionController.stealDesiredCard(target, current, desiredCard);
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
        lastPlayedCard = null;
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
        undoLastAction();
        lastPlayedCard = null;
    }

    private void undoLastAction() {
        switch (lastPlayedCard) {
            case SKIP:
                returnTurnToLastPlayer();
                break;
            case REVERSE:
                actionController.reverseDirection(turnTracker);
                returnTurnToLastPlayer();
                break;
            case ATTACK:
            case TARGETED_ATTACK:
                forcedTurns = forcedTurnsAfterUndoingAttack();
                turnTracker.setCurrentPlayer(lastPlayerId);
                break;
            case SEE_THE_FUTURE:
                actionController.shuffleDeck(deck);
                break;
            default:
                break;
        }
    }

    public List<Card> playClone(int targetId, int cardIndex) {
        ruleManager.requireSomethingToClone(lastPlayedCard);
        Player cloner = getPlayer(getCurrentPlayerId());
        CardType lastCard = lastPlayedCard;
        playFromHand(CardType.CLONE);
        lastPlayedCard = lastCard;
        cloner.addCardToHand(new Card(lastPlayedCard));
        List<Card> viewedCards = cloneLastAction(targetId, cardIndex);
        lastPlayedCard = null;
        return viewedCards;
    }

    private List<Card> cloneLastAction(int targetId, int cardIndex) {
        switch (lastPlayedCard) {
            case SKIP:
                playSkip();
                break;
            case REVERSE:
                playReverse();
                break;
            case ATTACK:
                playAttack();
                break;
            case SEE_THE_FUTURE:
                return playSeeTheFuture();
            case SHUFFLE:
                playShuffle();
                break;
            case TARGETED_ATTACK:
                playTargetedAttack(targetId);
                break;
            case FAVOR:
                playFavor(targetId, cardIndex);
                break;
            case SUPER_SKIP:
                playSuperSkip();
                break;
            case PERSONAL_ATTACK_3X:
                playPersonalAttack3X();
                break;
            default:
                break;
        }
        return new ArrayList<>();
    }

    public void playSuperSkip() {
        playFromHand(CardType.SUPER_SKIP);
        advanceToNextPlayer();
        forcedTurns = 1;
    }

    public void playBury(int index) {
        ruleManager.requireValidInsertIndex(index, getDrawPileSize());
        playFromHand(CardType.BURY);
        Card firstCard = deck.drawTop();
        deck.insertAt(firstCard, index);
        consumeOneForcedTurn();
    }

    public void playPersonalAttack3X() {
        playFromHand(CardType.PERSONAL_ATTACK_3X);
        forcedTurns += TURNS_ADDED_PERSONAL_ATTACK;
    }

    private void returnTurnToLastPlayer() {
        turnTracker.setCurrentPlayer(lastPlayerId);
        forcedTurns = NORMAL_FORCED_TURNS;
    }

    private int forcedTurnsAfterUndoingAttack() {
        int reduced = forcedTurns - TURNS_ADDED_BY_ATTACK;
        return reduced < NORMAL_FORCED_TURNS ? NORMAL_FORCED_TURNS : reduced;
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

    public int countAlive() {
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

    public void playFromHand(CardType type) {
        ruleManager.requirePlayable(type);
        Player current = getPlayer(getCurrentPlayerId());
        int index = current.getIndexOfCard(type);
        if (index < 0) {
            throw new IllegalStateException(NOT_IN_HAND_KEY);
        }
        deck.discard(current.removeCardFromHand(index));
        recordPlay(type);
    }

    private void recordPlay(CardType type) {
        lastPlayedCard = type;
        lastPlayerId = getCurrentPlayerId();
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

    static List<Card> buildShuffledNonSpecialPool() {
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

    static Deck buildRiggedDeck(int numPlayers, List<Card> remainingNonSpecial) {
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
