package domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class GameModelTest {

    @Test
    void startGame_twoPlayers_gameStarted() {
        GameModel model = new GameModel();
        model.startGame(List.of("P0", "P1"));
        assertTrue(model.isGameStarted());
    }

    @Test
    void startGame_twoPlayers_firstPlayerP0() {
        GameModel model = new GameModel();
        model.startGame(List.of("P0", "P1"));

        String expectedLocalPlayer = "P0";

        assertEquals(expectedLocalPlayer, model.getLocalPlayerName());
    }

    @Test
    void startGame_fivePlayers_gameStarted() {
        GameModel model = new GameModel();
        model.startGame(List.of("P0", "P1", "P2", "P3", "P4"));
        assertTrue(model.isGameStarted());
    }

    @Test
    void startGame_fivePlayers_firstPlayerP0() {
        GameModel model = new GameModel();
        model.startGame(List.of("P0", "P1", "P2", "P3", "P4"));

        String expectedLocalPlayer = "P0";

        assertEquals(expectedLocalPlayer, model.getLocalPlayerName());
    }

    @Test
    void startGame_onePlayer_throwsIllegalArgumentException() {
        GameModel model = new GameModel();

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> model.startGame(List.of("P0"))
        );

        String expectedMessage = "gameEngine.numPlayers.outOfRange";

        assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    void startGame_sixPlayer_throwsIllegalArgumentException() {
        GameModel model = new GameModel();

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> model.startGame(List.of("P0", "P1", "P2", "P3", "P4", "P5"))
        );

        String expectedMessage = "gameEngine.numPlayers.outOfRange";

        assertEquals(expectedMessage, ex.getMessage());
    }
}
