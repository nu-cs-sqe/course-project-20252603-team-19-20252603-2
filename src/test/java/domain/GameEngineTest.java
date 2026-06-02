package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class GameEngineTest {

    private static final int MIN_PLAYERS = 2;
    private static final int THREE_PLAYERS = 3;
    private static final int MAX_PLAYERS = 5;
    private static final int TOO_FEW = 1;
    private static final int TOO_MANY = 6;
    private static final int STARTING_HAND_SIZE = 5;
    private static final int SEE_THE_FUTURE_COUNT = 3;
    private static final int STACKED_ATTACK_FORCED_TURNS = 4;
    private static final int DRAW_PILE_SIZE_MIN_PLAYERS = 50;
    private static final int DRAW_PILE_SIZE_MAX_PLAYERS = 38;

    @Test
    void constructor_minPlayers_succeeds() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        assertEquals(MIN_PLAYERS, engine.getNumPlayers());
    }

    @Test
    void constructor_maxPlayers_succeeds() {
        GameEngine engine = new GameEngine(MAX_PLAYERS);
        assertEquals(MAX_PLAYERS, engine.getNumPlayers());
    }

    @Test
    void constructor_oneBelowMin_throwsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new GameEngine(TOO_FEW));
        assertEquals("gameEngine.numPlayers.outOfRange", ex.getMessage());
    }

    @Test
    void constructor_oneAboveMax_throwsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new GameEngine(TOO_MANY));
        assertEquals("gameEngine.numPlayers.outOfRange", ex.getMessage());
    }

    @Test
    void constructor_zero_throwsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new GameEngine(0));
        assertEquals("gameEngine.numPlayers.outOfRange", ex.getMessage());
    }

    @Test
    void constructor_negative_throwsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new GameEngine(-1));
        assertEquals("gameEngine.numPlayers.outOfRange", ex.getMessage());
    }

    @Test
    void getPlayer_returnsPlayerWithRequestedId_first() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        assertEquals(0, engine.getPlayer(0).getPlayerId());
    }

    @Test
    void getPlayer_returnsPlayerWithRequestedId_last() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        assertEquals(1, engine.getPlayer(1).getPlayerId());
    }

    @Test
    void getPlayer_negativeId_throwsIllegalArgumentException() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    engine.getPlayer(-1);
                });
        assertEquals("gameEngine.getPlayer.invalidId", ex.getMessage());
    }

    @Test
    void getPlayer_idEqualToNumPlayers_throwsIllegalArgumentException() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    engine.getPlayer(MIN_PLAYERS);
                });
        assertEquals("gameEngine.getPlayer.invalidId", ex.getMessage());
    }

    @Test
    void getCurrentPlayerId_atGameStart_minPlayers_returnsZero() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        assertEquals(0, engine.getCurrentPlayerId());
    }

    @Test
    void getCurrentPlayerId_atGameStart_maxPlayers_returnsZero() {
        GameEngine engine = new GameEngine(MAX_PLAYERS);
        assertEquals(0, engine.getCurrentPlayerId());
    }

    @Test
    void afterSetup_eachPlayerHasFiveCards_minPlayers() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        for (int id = 0; id < MIN_PLAYERS; id++) {
            assertEquals(STARTING_HAND_SIZE, engine.getPlayer(id).getHandSize());
        }
    }

    @Test
    void afterSetup_eachPlayerHasFiveCards_maxPlayers() {
        GameEngine engine = new GameEngine(MAX_PLAYERS);
        for (int id = 0; id < MAX_PLAYERS; id++) {
            assertEquals(STARTING_HAND_SIZE, engine.getPlayer(id).getHandSize());
        }
    }

    @Test
    void afterSetup_eachPlayerHasOneDefuse_minPlayers() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        for (int id = 0; id < MIN_PLAYERS; id++) {
            assertTrue(engine.getPlayer(id).hasCard(CardType.DEFUSE));
        }
    }

    @Test
    void afterSetup_noPlayerHasExplodingKitten_minPlayers() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        for (int id = 0; id < MIN_PLAYERS; id++) {
            assertFalse(engine.getPlayer(id).hasCard(CardType.EXPLODING_KITTEN));
        }
    }

    @Test
    void afterSetup_noPlayerHasExplodingKitten_maxPlayers() {
        GameEngine engine = new GameEngine(MAX_PLAYERS);
        for (int id = 0; id < MAX_PLAYERS; id++) {
            assertFalse(engine.getPlayer(id).hasCard(CardType.EXPLODING_KITTEN));
        }
    }

    @Test
    void getDrawPileSize_minPlayers_returns50() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        assertEquals(DRAW_PILE_SIZE_MIN_PLAYERS, engine.getDrawPileSize());
    }

    @Test
    void getDrawPileSize_maxPlayers_returns38() {
        GameEngine engine = new GameEngine(MAX_PLAYERS);
        assertEquals(DRAW_PILE_SIZE_MAX_PLAYERS, engine.getDrawPileSize());
    }

    @Test
    void isDeckEmpty_atGameStart_returnsFalse() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        assertFalse(engine.isDeckEmpty());
    }

    @Test
    void getPlayerHand_atGameStart_returnsStartingHand() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        assertEquals(STARTING_HAND_SIZE, engine.getPlayerHand(0).size());
    }

    @Test
    void getPlayerHand_returnedListIsDefensiveCopy() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        engine.getPlayerHand(0).clear();
        assertEquals(STARTING_HAND_SIZE, engine.getPlayerHand(0).size());
    }

    @Test
    void getPlayerHand_negativeId_throwsIllegalArgumentException() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    engine.getPlayerHand(-1);
                });
        assertEquals("gameEngine.getPlayer.invalidId", ex.getMessage());
    }

    @Test
    void getPlayerHand_idEqualToNumPlayers_throwsIllegalArgumentException() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    engine.getPlayerHand(MIN_PLAYERS);
                });
        assertEquals("gameEngine.getPlayer.invalidId", ex.getMessage());
    }

    @Test
    void drawCardForCurrentPlayer_movesTopCardIntoCurrentPlayerHand() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        int currentId = engine.getCurrentPlayerId();
        int pileBefore = engine.getDrawPileSize();

        Card drawn = engine.drawCardForCurrentPlayer();

        assertNotNull(drawn);
        assertEquals(STARTING_HAND_SIZE + 1, engine.getPlayerHand(currentId).size());
        assertEquals(pileBefore - 1, engine.getDrawPileSize());
    }

    @Test
    void drawCardForCurrentPlayer_emptyDeck_throwsIllegalStateExceptionAndDeckIsEmpty() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        while (!engine.isDeckEmpty()) {
            engine.drawCardForCurrentPlayer();
        }
        assertTrue(engine.isDeckEmpty());
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> {
                    engine.drawCardForCurrentPlayer();
                });
        assertEquals("deck.emptyType", ex.getMessage());
    }

    @Test
    void advanceToNextPlayer_once_movesToNextPlayer() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        engine.advanceToNextPlayer();
        assertEquals(1, engine.getCurrentPlayerId());
    }

    @Test
    void advanceToNextPlayer_twice_wrapsBackToFirstPlayer() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        engine.advanceToNextPlayer();
        engine.advanceToNextPlayer();
        assertEquals(0, engine.getCurrentPlayerId());
    }

    @Test
    void playSkip_endsTurnWithoutDrawing() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.SKIP);
        int pileBefore = engine.getDrawPileSize();

        engine.playSkip();

        assertEquals(1, engine.getCurrentPlayerId());
        assertEquals(pileBefore, engine.getDrawPileSize());
    }

    @Test
    void playSkip_withoutSkipInHand_throwsIllegalStateException() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        clearCardType(engine.getPlayer(0), CardType.SKIP);
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> engine.playSkip());
        assertEquals("gameEngine.play.notInHand", ex.getMessage());
    }

    @Test
    void playShuffle_keepsSamePlayerAndDeckSize() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.SHUFFLE);
        int pileBefore = engine.getDrawPileSize();

        engine.playShuffle();

        assertEquals(0, engine.getCurrentPlayerId());
        assertEquals(pileBefore, engine.getDrawPileSize());
    }

    @Test
    void playSeeTheFuture_returnsTopThreeAndKeepsSamePlayer() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.SEE_THE_FUTURE);

        assertEquals(SEE_THE_FUTURE_COUNT, engine.playSeeTheFuture().size());
        assertEquals(0, engine.getCurrentPlayerId());
    }

    @Test
    void playReverse_flipsDirectionAndEndsTurn() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.REVERSE);

        engine.playReverse();

        assertEquals(1, engine.getCurrentPlayerId());
    }

    @Test
    void playAttack_normalTurn_nextPlayerOwesTwoTurns() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.ATTACK);

        engine.playAttack();

        assertEquals(1, engine.getCurrentPlayerId());
        assertEquals(2, engine.getForcedTurns());
    }

    @Test
    void playAttack_stackedTurn_nextPlayerOwesFourTurns() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.ATTACK);
        engine.playAttack();
        giveToCurrent(engine, CardType.ATTACK);

        engine.playAttack();

        assertEquals(0, engine.getCurrentPlayerId());
        assertEquals(STACKED_ATTACK_FORCED_TURNS, engine.getForcedTurns());
    }

    @Test
    void playTargetedAttack_sendsTurnToChosenTargetWithTwoForcedTurns() {
        GameEngine engine = new GameEngine(THREE_PLAYERS);
        giveToCurrent(engine, CardType.TARGETED_ATTACK);

        engine.playTargetedAttack(2);

        assertEquals(2, engine.getCurrentPlayerId());
        assertEquals(2, engine.getForcedTurns());
    }

    @Test
    void playTargetedAttack_self_throwsIllegalArgumentException() {
        GameEngine engine = new GameEngine(THREE_PLAYERS);
        giveToCurrent(engine, CardType.TARGETED_ATTACK);
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> engine.playTargetedAttack(0));
        assertEquals("rule.target.invalid", ex.getMessage());
    }

    @Test
    void playFavor_takesCardFromTargetAndKeepsTurn() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.FAVOR);
        int targetHandBefore = engine.getPlayerHand(1).size();

        engine.playFavor(1, 0);

        assertEquals(targetHandBefore - 1, engine.getPlayerHand(1).size());
        assertEquals(0, engine.getCurrentPlayerId());
    }

    @Test
    void playFavor_self_throwsIllegalArgumentException() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.FAVOR);
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> engine.playFavor(0, 0));
        assertEquals("rule.target.invalid", ex.getMessage());
    }

    @Test
    void playFavor_invalidCardIndex_throwsIndexOutOfBoundsException() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.FAVOR);
        final int outOfRangeIndex = engine.getPlayerHand(1).size();
        IndexOutOfBoundsException ex = assertThrows(
                IndexOutOfBoundsException.class,
                () -> engine.playFavor(1, outOfRangeIndex));
        assertEquals("player.removeCardFromHand.invalidIndex", ex.getMessage());
    }

    @Test
    void playCatPair_stealsOneCardFromTargetAndKeepsTurn() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.CAT_CARDS);
        giveToCurrent(engine, CardType.CAT_CARDS);
        int targetHandBefore = engine.getPlayerHand(1).size();

        engine.playCatPair(1);

        assertEquals(targetHandBefore - 1, engine.getPlayerHand(1).size());
        assertEquals(0, engine.getCurrentPlayerId());
    }

    @Test
    void playCatPair_withoutTwoCats_throwsIllegalStateException() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        clearCardType(engine.getPlayer(0), CardType.CAT_CARDS);
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> engine.playCatPair(1));
        assertEquals("rule.catPair.needTwo", ex.getMessage());
    }

    @Test
    void defuseDrawnKitten_survivesAndReinsertsKitten() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.EXPLODING_KITTEN);
        final int pileBefore = engine.getDrawPileSize();

        engine.defuseDrawnKitten(0);

        assertTrue(engine.getPlayer(0).isAlive());
        assertEquals(-1, engine.getPlayer(0).getIndexOfCard(CardType.EXPLODING_KITTEN));
        assertEquals(pileBefore + 1, engine.getDrawPileSize());
        assertEquals(1, engine.getCurrentPlayerId());
    }

    @Test
    void defuseDrawnKitten_noKitten_throwsIllegalStateException() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> engine.defuseDrawnKitten(0));
        assertEquals("gameEngine.defuse.noKitten", ex.getMessage());
    }

    @Test
    void defuseDrawnKitten_noDefuse_throwsIllegalStateException() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        clearCardType(engine.getPlayer(0), CardType.DEFUSE);
        giveToCurrent(engine, CardType.EXPLODING_KITTEN);
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> engine.defuseDrawnKitten(0));
        assertEquals("gameEngine.defuse.noDefuse", ex.getMessage());
    }

    @Test
    void explodeCurrentPlayer_killsPlayerAndPassesTurn() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.EXPLODING_KITTEN);

        engine.explodeCurrentPlayer();

        assertFalse(engine.getPlayer(0).isAlive());
        assertEquals(1, engine.getCurrentPlayerId());
    }

    @Test
    void explodeCurrentPlayer_noKitten_throwsIllegalStateException() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> engine.explodeCurrentPlayer());
        assertEquals("gameEngine.defuse.noKitten", ex.getMessage());
    }

    @Test
    void isGameOver_atGameStart_returnsFalse() {
        assertFalse(new GameEngine(MIN_PLAYERS).isGameOver());
    }

    @Test
    void isGameOver_oneAlive_returnsTrue() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        engine.getPlayer(0).markDead();
        assertTrue(engine.isGameOver());
    }

    @Test
    void isGameOver_deckExhausted_returnsTrue() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        while (!engine.isDeckEmpty()) {
            engine.drawCardForCurrentPlayer();
        }
        assertTrue(engine.isGameOver());
    }

    @Test
    void getWinnerId_notOver_throwsIllegalStateException() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> engine.getWinnerId());
        assertEquals("gameEngine.notOver", ex.getMessage());
    }

    @Test
    void getWinnerId_lastPlayerStanding_returnsSurvivor() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        engine.getPlayer(0).markDead();
        assertEquals(1, engine.getWinnerId());
    }

    @Test
    void getWinnerId_deckExhausted_returnsPlayerWithMostCards() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        while (!engine.isDeckEmpty()) {
            engine.drawCardForCurrentPlayer();
        }
        assertEquals(0, engine.getWinnerId());
    }

    @Test
    void getWinnerId_deckExhaustedTie_returnsLowestId() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        while (!engine.isDeckEmpty()) {
            engine.drawCardForCurrentPlayer();
        }
        Player first = engine.getPlayer(0);
        Player second = engine.getPlayer(1);
        while (first.getHandSize() > second.getHandSize()) {
            first.removeCardFromHand(0);
        }
        assertEquals(0, engine.getWinnerId());
    }

    @Test
    void playNope_clearsLastPlayedCard() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.SKIP);
        engine.playSkip();
        engine.getPlayer(1).addCardToHand(new Card(CardType.NOPE));

        engine.playNope(1);

        assertNull(engine.getLastPlayedCard());
    }

    @Test
    void playNope_nothingToCancel_throwsIllegalStateException() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> engine.playNope(0));
        assertEquals("rule.nope.nothingToCancel", ex.getMessage());
    }

    @Test
    void playNope_noperLacksNope_throwsIllegalStateException() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.SKIP);
        engine.playSkip();
        clearCardType(engine.getPlayer(1), CardType.NOPE);
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> engine.playNope(1));
        assertEquals("gameEngine.play.notInHand", ex.getMessage());
    }

    @Test
    void endTurnByDrawing_normalTurn_advancesToNextPlayer() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        engine.endTurnByDrawing();
        assertEquals(1, engine.getCurrentPlayerId());
        assertEquals(1, engine.getForcedTurns());
    }

    @Test
    void endTurnByDrawing_underAttack_keepsSamePlayer() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.ATTACK);
        engine.playAttack();
        int attackedPlayer = engine.getCurrentPlayerId();

        engine.endTurnByDrawing();

        assertEquals(attackedPlayer, engine.getCurrentPlayerId());
        assertEquals(1, engine.getForcedTurns());
    }

    @Test
    void endTurnByDrawing_nextSeatDead_skipsToLivingPlayer() {
        GameEngine engine = new GameEngine(THREE_PLAYERS);
        engine.getPlayer(1).markDead();
        engine.endTurnByDrawing();
        assertEquals(2, engine.getCurrentPlayerId());
    }

    @Test
    void advanceToNextPlayer_skipsDeadPlayer() {
        GameEngine engine = new GameEngine(THREE_PLAYERS);
        engine.getPlayer(1).markDead();
        engine.advanceToNextPlayer();
        assertEquals(2, engine.getCurrentPlayerId());
    }

    @Test
    void getDiscardPile_atGameStart_isEmpty() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        assertEquals(0, engine.getDiscardPile().size());
    }

    @Test
    void getDiscardPile_afterPlay_containsPlayedCard() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.SKIP);
        engine.playSkip();
        assertEquals(1, engine.getDiscardPile().size());
        assertEquals(CardType.SKIP, engine.getDiscardPile().get(0).getCardType());
    }

    @Test
    void getDiscardPile_returnedListIsDefensiveCopy() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.SKIP);
        engine.playSkip();
        engine.getDiscardPile().clear();
        assertEquals(1, engine.getDiscardPile().size());
    }

    @Test
    void explodeCurrentPlayer_discardsEliminatedPlayersHand() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.EXPLODING_KITTEN);
        engine.explodeCurrentPlayer();
        assertEquals(0, engine.getPlayer(0).getHandSize());
    }

    private void giveToCurrent(GameEngine engine, CardType type) {
        engine.getPlayer(engine.getCurrentPlayerId()).addCardToHand(new Card(type));
    }

    private void clearCardType(Player player, CardType type) {
        int index = player.getIndexOfCard(type);
        while (index >= 0) {
            player.removeCardFromHand(index);
            index = player.getIndexOfCard(type);
        }
    }
}
