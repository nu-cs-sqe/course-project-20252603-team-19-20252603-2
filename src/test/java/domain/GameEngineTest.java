package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class GameEngineTest {

    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 5;
    private static final int TOO_FEW = 1;
    private static final int TOO_MANY = 6;
    private static final int STARTING_HAND_SIZE = 5;
    private static final int DRAW_PILE_SIZE_MIN_PLAYERS = 43;
    private static final int DRAW_PILE_SIZE_MAX_PLAYERS = 31;

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
    void getDrawPileSize_minPlayers_returns43() {
        GameEngine engine = new GameEngine(MIN_PLAYERS);
        assertEquals(DRAW_PILE_SIZE_MIN_PLAYERS, engine.getDrawPileSize());
    }

    @Test
    void getDrawPileSize_maxPlayers_returns31() {
        GameEngine engine = new GameEngine(MAX_PLAYERS);
        assertEquals(DRAW_PILE_SIZE_MAX_PLAYERS, engine.getDrawPileSize());
    }
}
