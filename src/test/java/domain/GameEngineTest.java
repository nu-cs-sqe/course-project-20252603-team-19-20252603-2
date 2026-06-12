package domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
    private static final int DRAW_PILE_SIZE_MIN_PLAYERS = 61;
    private static final int DRAW_PILE_SIZE_MAX_PLAYERS = 49;

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
    void getDrawPileSize_minPlayers_returns61() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        assertEquals(DRAW_PILE_SIZE_MIN_PLAYERS, engine.getDrawPileSize());
    }

    @Test
    void getDrawPileSize_maxPlayers_returns49() {
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
    void playShuffle_verifyShuffleIsDiscarded() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        clearHand(engine.getPlayer(0));
        giveToCurrent(engine, CardType.SHUFFLE);

        final int expectedHandSize = 0;
        final int beforeDiscardSize = engine.getDiscardPile().size();
        final boolean hasShuffle = false;

        engine.playShuffle();

        assertFalse(hasShuffle);
        assertEquals(expectedHandSize, engine.getPlayerHand(0).size());
        assertEquals(
                beforeDiscardSize + 1,
                engine.getDiscardPile().size()
        );
    }

    @Test
    void playShuffle_verifyShuffleIsCalled() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.SHUFFLE);

        List<CardType> before = engine.getDrawPile().stream()
                .map(Card::getCardType)
                .collect(Collectors.toList());

        boolean changed = false;
        final int playTimes = 20;

        for (int i = 0; i < playTimes; i++) {
            engine.playShuffle();
            List<CardType> after = engine.getDrawPile().stream()
                    .map(Card::getCardType)
                    .collect(Collectors.toList());
            if (!before.equals(after)) {
                changed = true;
                break;
            }
            giveToCurrent(engine, CardType.SHUFFLE);
        }

        assertTrue(changed);
    }

    @Test
    void playSeeTheFuture_returnsTopThreeAndKeepsSamePlayer() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.SEE_THE_FUTURE);

        assertEquals(SEE_THE_FUTURE_COUNT, engine.playSeeTheFuture().size());
        assertEquals(0, engine.getCurrentPlayerId());
    }

    @Test
    void playSeeTheFuture_verifySeeTheFutureIsPlayed() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        clearCardType(engine.getPlayer(0), CardType.SEE_THE_FUTURE);
        giveToCurrent(engine, CardType.SEE_THE_FUTURE);

        engine.playSeeTheFuture();

        assertEquals(
                -1,
                engine.getPlayer(0).getIndexOfCard(CardType.SEE_THE_FUTURE)
        );
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
    void playTargetedAttack_verifyTargetedAttackIsPlayed() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        clearCardType(engine.getPlayer(0), CardType.TARGETED_ATTACK);
        giveToCurrent(engine, CardType.TARGETED_ATTACK);

        engine.playTargetedAttack(1);

        assertEquals(
                -1,
                engine.getPlayer(0).getIndexOfCard(CardType.TARGETED_ATTACK)
        );
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
    void playCatPair_twoCatCards_stealsOneCardFromTargetAndKeepsTurn() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.CAT_CARDS);
        giveToCurrent(engine, CardType.CAT_CARDS);
        int targetHandBefore = engine.getPlayerHand(1).size();

        List<CardType> selectedCards = List.of(CardType.CAT_CARDS, CardType.CAT_CARDS);

        engine.playCatPair(1, selectedCards);

        assertEquals(targetHandBefore - 1, engine.getPlayerHand(1).size());
        assertEquals(0, engine.getCurrentPlayerId());
        assertEquals(CardType.CAT_CARDS, engine.getLastPlayedCard());
    }

    @Test
    void playCatPair_oneCatCardOneClone_stealsOneCardFromTargetAndKeepsTurn() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.CAT_CARDS);
        giveToCurrent(engine, CardType.CLONE);
        int targetHandBefore = engine.getPlayerHand(1).size();

        List<CardType> selectedCards = List.of(CardType.CAT_CARDS, CardType.CLONE);

        engine.playCatPair(1, selectedCards);

        assertEquals(targetHandBefore - 1, engine.getPlayerHand(1).size());
        assertEquals(0, engine.getCurrentPlayerId());
        assertEquals(CardType.CAT_CARDS, engine.getLastPlayedCard());
    }

    @Test
    void playCatPair_oneCatCardOneFeralCat_stealsOneCardFromTargetAndKeepsTurn() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.CAT_CARDS);
        giveToCurrent(engine, CardType.FERAL_CAT);
        int targetHandBefore = engine.getPlayerHand(1).size();

        List<CardType> selectedCards = List.of(CardType.CAT_CARDS, CardType.FERAL_CAT);

        engine.playCatPair(1, selectedCards);

        assertEquals(targetHandBefore - 1, engine.getPlayerHand(1).size());
        assertEquals(0, engine.getCurrentPlayerId());
        assertEquals(CardType.CAT_CARDS, engine.getLastPlayedCard());
    }

    @Test
    void playCatPair_withAnyMatchingPair_stealsAndRecordsThatCard() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.ATTACK);
        giveToCurrent(engine, CardType.ATTACK);
        int targetHandBefore = engine.getPlayerHand(1).size();

        List<CardType> selectedCards = List.of(CardType.ATTACK, CardType.ATTACK);

        engine.playCatPair(1, selectedCards);

        assertEquals(targetHandBefore - 1, engine.getPlayerHand(1).size());
        assertEquals(CardType.ATTACK, engine.getLastPlayedCard());
    }

    @Test
    void playCatPair_withoutTwoOfType_throwsIllegalStateException() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        clearCardType(engine.getPlayer(0), CardType.CAT_CARDS);
        clearCardType(engine.getPlayer(0), CardType.FERAL_CAT);
        clearCardType(engine.getPlayer(0), CardType.CLONE);
        giveToCurrent(engine, CardType.CAT_CARDS);
        List<CardType> selectedCards = List.of(CardType.CAT_CARDS);
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> engine.playCatPair(1, selectedCards));
        assertEquals("rule.catPair.needTwo", ex.getMessage());
    }

    @Test
    void playCatPair_twoFeralCat_throwsIllegalStateException() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        clearCardType(engine.getPlayer(0), CardType.FERAL_CAT);
        giveToCurrent(engine, CardType.FERAL_CAT);
        giveToCurrent(engine, CardType.FERAL_CAT);
        List<CardType> selectedCards = List.of(CardType.FERAL_CAT, CardType.FERAL_CAT);
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> engine.playCatPair(1, selectedCards));
        assertEquals("rule.catPair.feralCannotBeBaseType", ex.getMessage());
    }

    @Test
    void playCatPair_twoClone_throwsIllegalStateException() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        clearCardType(engine.getPlayer(0), CardType.CLONE);
        giveToCurrent(engine, CardType.CLONE);
        giveToCurrent(engine, CardType.CLONE);
        List<CardType> selectedCards = List.of(CardType.CLONE, CardType.CLONE);
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> engine.playCatPair(1, selectedCards));
        assertEquals("rule.catPair.cloneCannotBeBaseType", ex.getMessage());
    }

    @Test
    void playCatPair_verifyRequireValidTargetIsCalled() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        clearCardType(engine.getPlayer(0), CardType.CAT_CARDS);
        giveToCurrent(engine, CardType.CAT_CARDS);
        giveToCurrent(engine, CardType.CAT_CARDS);

        List<CardType> pair = List.of(
                CardType.CAT_CARDS,
                CardType.CAT_CARDS
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> engine.playCatPair(
                        engine.getCurrentPlayerId(),
                        pair
                )
        );
    }

    @Test
    void playCatPair_verifyCardDiscard() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        clearCardType(engine.getPlayer(0), CardType.CAT_CARDS);
        giveToCurrent(engine, CardType.CAT_CARDS);
        giveToCurrent(engine, CardType.CAT_CARDS);

        final int beforeDiscardPileSize = engine.getDiscardPile().size();

        List<CardType> pair = List.of(
                CardType.CAT_CARDS,
                CardType.CAT_CARDS
        );

        engine.playCatPair(
                1,
                pair
        );

        final int afterDiscardPileSize = engine.getDiscardPile().size();

        assertNotEquals(beforeDiscardPileSize, afterDiscardPileSize);
        assertEquals(beforeDiscardPileSize + 2, afterDiscardPileSize);
    }

    @Test
    void playCatTriple_targetHasDesiredCard_stealsItAndKeepsTurn() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.ATTACK);
        giveToCurrent(engine, CardType.ATTACK);
        giveToCurrent(engine, CardType.ATTACK);

        List<CardType> selectedCards = List.of(CardType.ATTACK, CardType.ATTACK, CardType.ATTACK);

        engine.playCatTriple(1, selectedCards, CardType.DEFUSE);

        assertFalse(engine.getPlayer(1).hasCard(CardType.DEFUSE));
        assertTrue(engine.getPlayer(0).hasCard(CardType.DEFUSE));
        assertEquals(0, engine.getCurrentPlayerId());
        assertEquals(CardType.ATTACK, engine.getLastPlayedCard());
    }

    @Test
    void playCatTriple_targetLacksDesiredCard_stealsNothing() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.ATTACK);
        giveToCurrent(engine, CardType.ATTACK);
        giveToCurrent(engine, CardType.ATTACK);
        int targetHandBefore = engine.getPlayerHand(1).size();

        List<CardType> selectedCards = List.of(CardType.ATTACK, CardType.ATTACK, CardType.ATTACK);

        engine.playCatTriple(1, selectedCards, CardType.EXPLODING_KITTEN);

        assertEquals(targetHandBefore, engine.getPlayerHand(1).size());
        assertEquals(CardType.ATTACK, engine.getLastPlayedCard());
    }

    @Test
    void playCatTriple_withoutThreeOfType_throwsIllegalStateException() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        clearCardType(engine.getPlayer(0), CardType.ATTACK);
        giveToCurrent(engine, CardType.ATTACK);
        giveToCurrent(engine, CardType.ATTACK);

        List<CardType> selectedCards = List.of(CardType.ATTACK, CardType.ATTACK);

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> engine.playCatTriple(1, selectedCards, CardType.DEFUSE));
        assertEquals("rule.catTriple.needThree", ex.getMessage());
    }

    @Test
    void playCatTriple_invalidThreeCardCombo_throwsIllegalStateException() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        clearCardType(engine.getPlayer(0), CardType.ATTACK);
        clearCardType(engine.getPlayer(0), CardType.DEFUSE);
        clearCardType(engine.getPlayer(0), CardType.FAVOR);
        giveToCurrent(engine, CardType.ATTACK);
        giveToCurrent(engine, CardType.DEFUSE);
        giveToCurrent(engine, CardType.FAVOR);

        List<CardType> selectedCards = List.of(CardType.ATTACK, CardType.DEFUSE, CardType.FAVOR);

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> engine.playCatTriple(1, selectedCards, CardType.DEFUSE));
        assertEquals("rule.catTriple.needThree", ex.getMessage());
    }

    @Test
    void playCatTriple_twoCatCardAndOneFeralCat_stealsItAndKeepsTurn() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.CAT_CARDS);
        giveToCurrent(engine, CardType.CAT_CARDS);
        giveToCurrent(engine, CardType.FERAL_CAT);

        List<CardType> selectedCards = List.of(
                CardType.CAT_CARDS, CardType.CAT_CARDS, CardType.FERAL_CAT
        );

        engine.playCatTriple(1, selectedCards, CardType.DEFUSE);

        assertFalse(engine.getPlayer(1).hasCard(CardType.DEFUSE));
        assertTrue(engine.getPlayer(0).hasCard(CardType.DEFUSE));
        assertEquals(0, engine.getCurrentPlayerId());
        assertEquals(CardType.CAT_CARDS, engine.getLastPlayedCard());
    }

    @Test
    void playCatTriple_oneCatCardAndTwoFeralCat_stealsItAndKeepsTurn() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.CAT_CARDS);
        giveToCurrent(engine, CardType.FERAL_CAT);
        giveToCurrent(engine, CardType.FERAL_CAT);

        List<CardType> selectedCards = List.of(
                CardType.CAT_CARDS, CardType.FERAL_CAT, CardType.FERAL_CAT
        );

        engine.playCatTriple(1, selectedCards, CardType.DEFUSE);

        assertFalse(engine.getPlayer(1).hasCard(CardType.DEFUSE));
        assertTrue(engine.getPlayer(0).hasCard(CardType.DEFUSE));
        assertEquals(0, engine.getCurrentPlayerId());
        assertEquals(CardType.CAT_CARDS, engine.getLastPlayedCard());
    }

    @Test
    void playCatTriple_threeFeralCat_throwsIllegalStateException() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        clearCardType(engine.getPlayer(0), CardType.FERAL_CAT);
        giveToCurrent(engine, CardType.FERAL_CAT);
        giveToCurrent(engine, CardType.FERAL_CAT);
        giveToCurrent(engine, CardType.FERAL_CAT);

        List<CardType> selectedCards = List.of(
                CardType.FERAL_CAT, CardType.FERAL_CAT, CardType.FERAL_CAT
        );

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> engine.playCatTriple(1, selectedCards, CardType.DEFUSE));
        assertEquals("rule.catTriple.feralCannotBeBaseType", ex.getMessage());
    }

    @Test
    void playCatTriple_twoCatCardAndOneClone_stealsItAndKeepsTurn() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.CAT_CARDS);
        giveToCurrent(engine, CardType.CAT_CARDS);
        giveToCurrent(engine, CardType.CLONE);

        List<CardType> selectedCards = List.of(
                CardType.CAT_CARDS, CardType.CAT_CARDS, CardType.CLONE
        );

        engine.playCatTriple(1, selectedCards, CardType.DEFUSE);

        assertFalse(engine.getPlayer(1).hasCard(CardType.DEFUSE));
        assertTrue(engine.getPlayer(0).hasCard(CardType.DEFUSE));
        assertEquals(0, engine.getCurrentPlayerId());
        assertEquals(CardType.CAT_CARDS, engine.getLastPlayedCard());
    }

    @Test
    void playCatTriple_oneCatCardTwoClone_throwsIllegalStateException() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        clearCardType(engine.getPlayer(engine.getCurrentPlayerId()), CardType.CAT_CARDS);
        clearCardType(engine.getPlayer(engine.getCurrentPlayerId()), CardType.CLONE);
        clearCardType(engine.getPlayer(engine.getCurrentPlayerId()), CardType.FERAL_CAT);
        giveToCurrent(engine, CardType.CAT_CARDS);
        giveToCurrent(engine, CardType.CLONE);
        giveToCurrent(engine, CardType.CLONE);

        List<CardType> selectedCards = List.of(
                CardType.CAT_CARDS, CardType.CLONE, CardType.CLONE
        );

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> engine.playCatTriple(1, selectedCards, CardType.DEFUSE));
        assertEquals("rule.catTriple.needThree", ex.getMessage());
    }

    @Test
    void playCatTriple_threeClone_throwsIllegalStateException() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        clearCardType(engine.getPlayer(engine.getCurrentPlayerId()), CardType.CLONE);
        giveToCurrent(engine, CardType.CLONE);
        giveToCurrent(engine, CardType.CLONE);
        giveToCurrent(engine, CardType.CLONE);

        List<CardType> selectedCards = List.of(CardType.CLONE, CardType.CLONE, CardType.CLONE);

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> engine.playCatTriple(1, selectedCards, CardType.DEFUSE));
        assertEquals("rule.catTriple.cloneCannotBeBaseType", ex.getMessage());
    }

    @Test
    void playCatTriple_verifyRequireValidTargetIsCalled() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        clearCardType(engine.getPlayer(0), CardType.CAT_CARDS);
        giveToCurrent(engine, CardType.CAT_CARDS);
        giveToCurrent(engine, CardType.CAT_CARDS);
        giveToCurrent(engine, CardType.CAT_CARDS);

        List<CardType> triple = List.of(
                CardType.CAT_CARDS,
                CardType.CAT_CARDS,
                CardType.CAT_CARDS
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> engine.playCatTriple(
                        engine.getCurrentPlayerId(),
                        triple,
                        CardType.ATTACK
                )
        );
    }

    @Test
    void playCatTriple_verifyCardDiscard() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        clearCardType(engine.getPlayer(0), CardType.CAT_CARDS);
        giveToCurrent(engine, CardType.CAT_CARDS);
        giveToCurrent(engine, CardType.CAT_CARDS);
        giveToCurrent(engine, CardType.CAT_CARDS);

        final int beforeDiscardPileSize = engine.getDiscardPile().size();

        List<CardType> triple = List.of(
                CardType.CAT_CARDS,
                CardType.CAT_CARDS,
                CardType.CAT_CARDS
        );

        engine.playCatTriple(
                1,
                triple,
                CardType.ATTACK
        );

        final int afterDiscardPileSize = engine.getDiscardPile().size();
        final int cardsPlayed = 3;

        assertNotEquals(beforeDiscardPileSize, afterDiscardPileSize);
        assertEquals(beforeDiscardPileSize + cardsPlayed, afterDiscardPileSize);
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
    void defuseDrawnKitten_kittenAtIndexZero_defusesCorrectly() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        clearHand(engine.getPlayer(0));
        giveToCurrent(engine, CardType.EXPLODING_KITTEN);
        giveToCurrent(engine, CardType.DEFUSE);

        assertDoesNotThrow(
                () -> engine.defuseDrawnKitten(0)
        );
    }

    @Test
    void defuseDrawnKitten_defuseAtIndexZero_defusesCorrectly() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        clearHand(engine.getPlayer(0));
        giveToCurrent(engine, CardType.DEFUSE);
        giveToCurrent(engine, CardType.EXPLODING_KITTEN);

        assertDoesNotThrow(
                () -> engine.defuseDrawnKitten(0)
        );
    }

    @Test
    void defuseDrawnKitten_verifyDefuseDiscard() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        clearHand(engine.getPlayer(0));
        giveToCurrent(engine, CardType.DEFUSE);
        giveToCurrent(engine, CardType.EXPLODING_KITTEN);

        final int discardPileSize = engine.getDiscardPile().size() + 1;

        engine.defuseDrawnKitten(0);

        assertEquals(discardPileSize, engine.getDiscardPile().size());
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
    void explodeCurrentPlayer_kittenAtIndexZero_explodesCorrectly() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        clearHand(engine.getPlayer(0));
        giveToCurrent(engine, CardType.EXPLODING_KITTEN);

        assertDoesNotThrow(
                () -> engine.explodeCurrentPlayer()
        );
    }

    @Test
    void explodeCurrentPlayer_properlyRemovesExplodingKittenFromHand() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        clearHand(engine.getPlayer(0));
        giveToCurrent(engine, CardType.EXPLODING_KITTEN);

        engine.explodeCurrentPlayer();

        final boolean hasKitten = engine.getPlayer(0).hasCard(CardType.EXPLODING_KITTEN);
        assertFalse(hasKitten);
    }

    @Test
    void explodeCurrentPlayer_properlyClearsHand() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        clearHand(engine.getPlayer(0));
        giveToCurrent(engine, CardType.CAT_CARDS);
        giveToCurrent(engine, CardType.SHUFFLE);
        giveToCurrent(engine, CardType.EXPLODING_KITTEN);

        final int expectedDiscardPileSize =
                engine.getDiscardPile().size();

        engine.explodeCurrentPlayer();

        final int cardsPlayed = 3;

        assertEquals(
                expectedDiscardPileSize + cardsPlayed,
                engine.getDiscardPile().size()
        );
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
    void playNope_onSkip_returnsTurnToThePlayerWhoPlayedIt() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.SKIP);
        engine.playSkip();
        engine.getPlayer(1).addCardToHand(new Card(CardType.NOPE));

        engine.playNope(1);

        assertEquals(0, engine.getCurrentPlayerId());
        assertEquals(1, engine.getForcedTurns());
    }

    @Test
    void playNope_onReverse_returnsTurnToThePlayerWhoPlayedIt() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.REVERSE);
        engine.playReverse();
        engine.getPlayer(1).addCardToHand(new Card(CardType.NOPE));

        engine.playNope(1);

        assertEquals(0, engine.getCurrentPlayerId());
    }

    @Test
    void playNope_onReverse_verifyDirectionIsReversed() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.REVERSE);
        engine.playReverse();
        engine.getPlayer(1).addCardToHand(new Card(CardType.NOPE));

        final int afterDirection = 1;
        engine.playNope(1);

        assertEquals(afterDirection, engine.getCurrentDirection());
    }

    @Test
    void playNope_onAttack_reducesForcedTurnsAndReturnsToAttacker() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.ATTACK);
        engine.playAttack();
        engine.getPlayer(1).addCardToHand(new Card(CardType.NOPE));

        engine.playNope(1);

        assertEquals(0, engine.getCurrentPlayerId());
        assertEquals(1, engine.getForcedTurns());
    }

    @Test
    void playNope_onTargetedAttack_returnsToAttacker() {
        GameEngine engine = new GameEngine(THREE_PLAYERS);
        giveToCurrent(engine, CardType.TARGETED_ATTACK);
        engine.playTargetedAttack(2);
        engine.getPlayer(2).addCardToHand(new Card(CardType.NOPE));

        engine.playNope(2);

        assertEquals(0, engine.getCurrentPlayerId());
        assertEquals(1, engine.getForcedTurns());
    }

    @Test
    void playNope_onSeeTheFuture_keepsTurnAndClearsLastPlayed() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.SEE_THE_FUTURE);
        engine.playSeeTheFuture();
        engine.getPlayer(1).addCardToHand(new Card(CardType.NOPE));

        engine.playNope(1);

        assertEquals(0, engine.getCurrentPlayerId());
        assertNull(engine.getLastPlayedCard());
    }

    @Test
    void playNope_onSeeTheFuture_verifyDeckIsShuffled() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.SEE_THE_FUTURE);
        giveToCurrent(engine, CardType.NOPE);

        List<CardType> before = engine.getDrawPile().stream()
                .map(Card::getCardType)
                .collect(Collectors.toList());

        boolean changed = false;

        final int timesPlayed = 10;

        for (int i = 0; i < timesPlayed; i++) {
            engine.playSeeTheFuture();
            engine.playNope(0);
            List<CardType> after = engine.getDrawPile().stream()
                    .map(Card::getCardType)
                    .collect(Collectors.toList());
            if (!before.equals(after)) {
                changed = true;
                break;
            }
            giveToCurrent(engine, CardType.SEE_THE_FUTURE);
            giveToCurrent(engine, CardType.NOPE);
        }

        assertTrue(changed);
    }

    @Test
    void playNope_on_keepsTurnAndClearsLastPlayed() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.FAVOR);
        engine.playFavor(1, 0);
        engine.getPlayer(1).addCardToHand(new Card(CardType.NOPE));

        engine.playNope(1);

        assertEquals(0, engine.getCurrentPlayerId());
        assertNull(engine.getLastPlayedCard());
    }

    @Test
    void playNope_nopeAtIndexZeroWithSeeTheFuture_undoSkipAction() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        clearHand(engine.getPlayer(0));
        clearHand(engine.getPlayer(1));
        giveToCurrent(engine, CardType.SEE_THE_FUTURE);
        engine.playSeeTheFuture();
        giveToCurrent(engine, CardType.NOPE);

        engine.playNope(0);

        assertEquals(0, engine.getCurrentPlayerId());
        assertEquals(1, engine.getForcedTurns());
    }

    @Test
    void playNope_discardsNopeCard() {
        GameEngine engine = new GameEngine(2);
        clearHand(engine.getPlayer(0));
        clearCardType(engine.getPlayer(1), CardType.NOPE);

        giveToCurrent(engine, CardType.SKIP);
        engine.playSkip();

        giveToCurrent(engine, CardType.NOPE);

        int beforeDiscard =
                engine.getDiscardPile().size();
        engine.playNope(1);

        List<Card> discardPile =
                engine.getDiscardPile();

        final boolean hasNope = engine.getPlayer(1).hasCard(CardType.NOPE);

        assertEquals(
                beforeDiscard + 1,
                discardPile.size()
        );

        assertEquals(
                CardType.NOPE,
                discardPile
                        .get(beforeDiscard)
                        .getCardType()
        );

        assertFalse(hasNope);
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

    @Test
    void playClone_clearsLastPlayedCard() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.SKIP);
        engine.playSkip();
        engine.getPlayer(1).addCardToHand(new Card(CardType.CLONE));

        engine.playClone(0, 0);

        assertNull(engine.getLastPlayedCard());
    }

    @Test
    void playClone_nothingToClone_throwsIllegalStateException() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> engine.playClone(0, 0));
        assertEquals("rule.clone.nothingToClone", ex.getMessage());
    }

    @Test
    void playClone_clonerLacksClone_throwsIllegalStateException() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.SKIP);
        engine.playSkip();
        clearCardType(engine.getPlayer(1), CardType.CLONE);
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> engine.playClone(0, 0));
        assertEquals("gameEngine.play.notInHand", ex.getMessage());
    }

    @Test
    void playClone_onSkip_turnGoesToTheNextLivingPlayer() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.SKIP);
        engine.playSkip();
        engine.getPlayer(1).addCardToHand(new Card(CardType.CLONE));

        engine.playClone(0, 0);

        assertEquals(0, engine.getCurrentPlayerId());
        assertEquals(1, engine.getForcedTurns());
    }

    @Test
    void playClone_onReverse_directionRestored() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.REVERSE);
        engine.playReverse();
        engine.getPlayer(1).addCardToHand(new Card(CardType.CLONE));

        assertEquals(-1, engine.getCurrentDirection());

        engine.playClone(0, 0);

        assertEquals(0, engine.getCurrentPlayerId());
        assertEquals(1, engine.getCurrentDirection());
    }

    @Test
    void playClone_onAttack_nextLivingPlayerDrawsFourTimes() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.ATTACK);
        engine.playAttack();
        engine.getPlayer(1).addCardToHand(new Card(CardType.CLONE));

        final int expectedForcedTurns = 4;

        engine.playClone(0, 0);

        assertEquals(0, engine.getCurrentPlayerId());
        assertEquals(expectedForcedTurns, engine.getForcedTurns());
    }

    @Test
    void playClone_onTargetedAttack_targetedPlayerDrawsFourTimes() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.TARGETED_ATTACK);
        engine.playTargetedAttack(1);
        engine.getPlayer(1).addCardToHand(new Card(CardType.CLONE));

        final int expectedForcedTurns = 4;

        engine.playClone(0, 0);

        assertEquals(0, engine.getCurrentPlayerId());
        assertEquals(expectedForcedTurns, engine.getForcedTurns());
    }

    @Test
    void playClone_onSeeTheFuture_returnsTopThreeAndKeepsSamePlayer() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.SEE_THE_FUTURE);
        engine.playSeeTheFuture();
        engine.getPlayer(0).addCardToHand(new Card(CardType.CLONE));

        assertEquals(SEE_THE_FUTURE_COUNT, engine.playClone(0, 0).size());
        assertEquals(0, engine.getCurrentPlayerId());
    }

    @Test
    void playClone_onShuffle_shufflesDeckAndKeepsSamePlayer() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        engine.setLastPlayedCard(CardType.SHUFFLE);
        giveToCurrent(engine, CardType.CLONE);

        int pileSize = engine.getDrawPileSize();

        List<CardType> before = engine.getDrawPile().stream()
                .map(Card::getCardType)
                .collect(Collectors.toList());

        boolean changed = false;
        final int timesPlayed = 10;

        for (int i = 0; i < timesPlayed; i++) {
            engine.playClone(0, 0);
            List<CardType> after = engine.getDrawPile().stream()
                    .map(Card::getCardType)
                    .collect(Collectors.toList());
            if (!before.equals(after)) {
                changed = true;
                break;
            }
            giveToCurrent(engine, CardType.CLONE);
            engine.setLastPlayedCard(CardType.SHUFFLE);
        }

        assertTrue(changed);
        assertEquals(pileSize, engine.getDrawPileSize());
        assertEquals(0, engine.getCurrentPlayerId());
    }

    @Test
    void playClone_onFavor_verifiesPlayFavorMethodIsCalledAndKeepsSamePlayer() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.FAVOR);
        engine.getPlayer(1).addCardToHand(new Card(CardType.CLONE));

        final int expectedHandSize = 6;

        assertEquals(expectedHandSize, engine.getPlayerHand(0).size());
        assertEquals(expectedHandSize, engine.getPlayerHand(1).size());

        engine.playFavor(1, 0);
        engine.advanceToNextPlayer();

        final int player0HandSize = 6;
        final int player1HandSize = 5;

        assertEquals(player0HandSize, engine.getPlayerHand(0).size());
        assertEquals(player1HandSize, engine.getPlayerHand(1).size());
        assertEquals(1, engine.getCurrentPlayerId());

        engine.playClone(0, 0);

        final int finalPlayerHandSize = 5;

        assertEquals(finalPlayerHandSize, engine.getPlayerHand(0).size());
        assertEquals(finalPlayerHandSize, engine.getPlayerHand(1).size());
        assertEquals(1, engine.getCurrentPlayerId());
    }

    @Test
    void playClone_onClone_throwsIllegalStateException() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        engine.setLastPlayedCard(CardType.CLONE);
        engine.getPlayer(0).addCardToHand(new Card(CardType.CLONE));

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            engine.playClone(0, 0);
        });

        assertEquals("rule.clone.cannotCloneClone", ex.getMessage());
    }

    @Test
    void playClone_onSuperSkip_forceTurnBecomeOneAndAdvancesToNextPlayer() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.ATTACK);
        engine.getPlayer(1).addCardToHand(new Card(CardType.CLONE));
        engine.playAttack();

        final int expectedForcedTurns = 2;

        assertEquals(expectedForcedTurns, engine.getForcedTurns());

        engine.setLastPlayedCard(CardType.SUPER_SKIP);
        engine.playClone(0, 0);

        assertEquals(0, engine.getCurrentPlayerId());
        assertEquals(1, engine.getForcedTurns());
    }

    @Test
    void playClone_onPersonalAttack3X_forceTurnBecomesFourAndSamePlayer() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.CLONE);
        engine.setLastPlayedCard(CardType.PERSONAL_ATTACK_3X);

        final int expectedForcedTurns = 4;

        engine.playClone(0, 0);

        assertEquals(0, engine.getCurrentPlayerId());
        assertEquals(expectedForcedTurns, engine.getForcedTurns());
    }

    @Test
    void playClone_onReverse_returnsMutableList() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.CLONE);
        engine.setLastPlayedCard(CardType.REVERSE);

        List<Card> result =
                engine.playClone(0, 0);

        assertEquals(
                ArrayList.class,
                result.getClass()
        );
        assertTrue(
                result.isEmpty()
        );
    }

    @Test
    void playClone_onInvalidCard_returnsMutableList() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.CLONE);
        engine.setLastPlayedCard(CardType.DEFUSE);

        List<Card> result =
                engine.playClone(0, 0);

        assertEquals(
                ArrayList.class,
                result.getClass()
        );
        assertTrue(
                result.isEmpty()
        );
    }

    @Test
    void playSuperSkip_endsTurnWithoutDrawing() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.SUPER_SKIP);
        int pileBefore = engine.getDrawPileSize();

        engine.playSuperSkip();

        assertEquals(1, engine.getCurrentPlayerId());
        assertEquals(pileBefore, engine.getDrawPileSize());
    }

    @Test
    void playSuperSkip_withoutSkipInHand_throwsIllegalStateException() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        clearCardType(engine.getPlayer(0), CardType.SUPER_SKIP);
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> engine.playSuperSkip());
        assertEquals("gameEngine.play.notInHand", ex.getMessage());
    }

    @Test
    void playSuperSkip_forcedTurnIsFour_endsTurnWithoutDrawingAndForcedTurnIsOne() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.ATTACK);
        giveToCurrent(engine, CardType.SUPER_SKIP);
        engine.getPlayer(1).addCardToHand(new Card(CardType.ATTACK));

        engine.playAttack();
        engine.playAttack();

        final int expectedDraws = 4;
        assertEquals(expectedDraws, engine.getForcedTurns());

        int pileBefore = engine.getDrawPileSize();
        final int drawsAfterSuperSkip = 1;

        engine.playSuperSkip();

        assertEquals(1, engine.getCurrentPlayerId());
        assertEquals(pileBefore, engine.getDrawPileSize());
        assertEquals(drawsAfterSuperSkip, engine.getForcedTurns());
    }

    @Test
    void playBury_onIndex1_topCardMovesToIndex1AndTurnAdvances() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);

        final int expectedDrawPileSize = 61;
        final int drawPileSize = engine.getDrawPileSize();

        assertEquals(expectedDrawPileSize, drawPileSize);

        Card firstCard = engine.getDrawPile().get(0);

        giveToCurrent(engine, CardType.BURY);

        engine.playBury(1);

        assertEquals(firstCard, engine.getDrawPile().get(1));
        assertEquals(1, engine.getCurrentPlayerId());
    }

    @Test
    void playBury_onIndex30_topCardMovesToIndex30AndTurnAdvances() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);

        final int expectedDrawPileSize = 61;
        final int drawPileSize = engine.getDrawPileSize();
        final int reinsertIndex = 30;

        assertEquals(expectedDrawPileSize, drawPileSize);

        Card firstCard = engine.getDrawPile().get(0);

        giveToCurrent(engine, CardType.BURY);

        engine.playBury(reinsertIndex);

        assertEquals(firstCard, engine.getDrawPile().get(reinsertIndex));
        assertEquals(1, engine.getCurrentPlayerId());
    }

    @Test
    void playBury_onIndex60_topCardMovesToIndex60AndTurnAdvances() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);

        final int expectedDrawPileSize = 61;
        final int drawPileSize = engine.getDrawPileSize();
        final int reinsertIndex = 60;

        assertEquals(expectedDrawPileSize, drawPileSize);

        Card firstCard = engine.getDrawPile().get(0);

        giveToCurrent(engine, CardType.BURY);

        engine.playBury(reinsertIndex);

        assertEquals(firstCard, engine.getDrawPile().get(reinsertIndex));
        assertEquals(1, engine.getCurrentPlayerId());
    }

    @Test
    void playBury_withoutBuryInHand_throwsIllegalStateException() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        clearCardType(engine.getPlayer(0), CardType.BURY);
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> engine.playBury(0));
        assertEquals("gameEngine.play.notInHand", ex.getMessage());
    }

    @Test
    void playBury_onIndex62_throwsIllegalStateException() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        final int index = 62;
        giveToCurrent(engine, CardType.BURY);
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> engine.playBury(index));
        assertEquals("rule.bury.invalidIndex", ex.getMessage());
    }

    @Test
    void playBury_onIndexNegativeOne_throwsIllegalStateException() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        final int index = -1;
        giveToCurrent(engine, CardType.BURY);
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> engine.playBury(index));
        assertEquals("rule.bury.invalidIndex", ex.getMessage());
    }

    @Test
    void playPersonalAttack3X_forcedTurnIs1_forcedturnsIs4AndSamePlayer() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        giveToCurrent(engine, CardType.PERSONAL_ATTACK_3X);

        final int initialForcedTurns = 1;
        final int expectedForcedTurns = 4;

        assertEquals(0, engine.getCurrentPlayerId());
        assertEquals(initialForcedTurns, engine.getForcedTurns());

        engine.playPersonalAttack3X();

        assertEquals(0, engine.getCurrentPlayerId());
        assertEquals(expectedForcedTurns, engine.getForcedTurns());
    }

    @Test
    void playPersonalAttack3X_withoutPersonalAttack3xInHand_throwsIllegalStateException() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        clearCardType(engine.getPlayer(0), CardType.PERSONAL_ATTACK_3X);
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> engine.playPersonalAttack3X());
        assertEquals("gameEngine.play.notInHand", ex.getMessage());
    }

    @Test
    void forcedTurnsAfterUndoingAttack_dropsBelowBaseline_resetsToNormal() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);

        clearCardType(engine.getPlayer(0), CardType.ATTACK);
        clearCardType(engine.getPlayer(1), CardType.NOPE);

        giveToCurrent(engine, CardType.ATTACK);
        engine.getPlayer(1).addCardToHand(new Card(CardType.NOPE));

        engine.playAttack();
        engine.playNope(1);

        assertEquals(1, engine.getForcedTurns());
    }

    @Test
    void forcedTurnsAfterUndoingAttack_remainsAboveBaseline_returnsReducedValue() {
        GameEngine engine = new GameEngine(2); // Starts on Player 0

        giveToCurrent(engine, CardType.ATTACK);

        engine.playAttack();
        assertEquals(1, engine.getCurrentPlayerId());

        giveToCurrent(engine, CardType.ATTACK);
        engine.playAttack();
        assertEquals(0, engine.getCurrentPlayerId());

        giveToCurrent(engine, CardType.NOPE);
        engine.playNope(0);

        assertEquals(2, engine.getForcedTurns());
    }

    @Test
    void countAlive_atGameStart_minPlayers_returnsTwo() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        assertEquals(MIN_PLAYERS, engine.countAlive());
    }

    @Test
    void countAlive_fivePlayersFourAlive_returnsFourAlive() {
        GameEngine engine = new GameEngine(MAX_PLAYERS);

        // kill current player
        Player current =
                engine.getPlayer(
                        engine.getCurrentPlayerId());

        current.markDead();

        final int expectedAlive = 4;

        assertEquals(
                expectedAlive,
                engine.countAlive()
        );
    }

    @Test
    void playFromHand_unplayableCard_throwsException() {
        GameEngine engine = new GameEngine(2);
        Player current =
                engine.getPlayer(
                        engine.getCurrentPlayerId());

        current.addCardToHand(
                new Card(CardType.DEFUSE));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> engine.playFromHand(CardType.DEFUSE));

        assertEquals(
                "rule.play.cannotPlayDirectly",
                ex.getMessage()
        );
    }

    @Test
    void playFromHand_skipNotInHand_throwsIllegalStateException() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        clearCardType(engine.getPlayer(0), CardType.SKIP);
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> engine.playFromHand(CardType.SKIP));
        assertEquals("gameEngine.play.notInHand", ex.getMessage());
    }

    @Test
    void buildShuffledNonSpecialPool_isRandomized() {
        List<Card> result1 = GameEngine.buildShuffledNonSpecialPool();
        List<Card> result2 = GameEngine.buildShuffledNonSpecialPool();

        List<CardType> order1 = result1.stream()
                .map(Card::getCardType)
                .collect(Collectors.toList());

        List<CardType> order2 = result2.stream()
                .map(Card::getCardType)
                .collect(Collectors.toList());

        assertNotEquals(order1, order2);
    }

    @Test
    void buildShuffledNonSpecialPool_hasMultiplePossibleOrders() {
        Set<List<CardType>> seenOrders = new HashSet<>();

        final int timesPlayed = 10;

        for (int i = 0; i < timesPlayed; i++) {
            List<Card> result = GameEngine.buildShuffledNonSpecialPool();

            List<CardType> order = result.stream()
                    .map(Card::getCardType)
                    .collect(Collectors.toList());

            seenOrders.add(order);
        }

        assertTrue(seenOrders.size() > 1);
    }

    @Test
    void buildRiggedDeck_isActuallyShuffled() {
        List<Card> base = new ArrayList<>();

        final int timesPlayed = 10;

        for (int i = 0; i < timesPlayed; i++) {
            base.add(new Card(CardType.CAT_CARDS));
        }

        Deck d1 = GameEngine.buildRiggedDeck(2, base);
        Deck d2 = GameEngine.buildRiggedDeck(2, base);

        List<CardType> order1 = d1.getDrawPile().stream()
                .map(Card::getCardType)
                .collect(Collectors.toList());

        List<CardType> order2 = d2.getDrawPile().stream()
                .map(Card::getCardType)
                .collect(Collectors.toList());

        assertNotEquals(order1, order2);
    }

    @Test
    public void advanceToNextLivingPlayer_skipsDeadPlayer() {
        final int numPlayers = 3;
        GameEngine engine = new GameEngine(numPlayers);

        engine.getPlayer(1).markDead();
        engine.advanceToNextPlayer();

        assertEquals(2, engine.getCurrentPlayerId());
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

    private void clearHand(Player player) {
        while (player.getHandSize() > 0) {
            player.removeCardFromHand(0);
        }
    }
}
