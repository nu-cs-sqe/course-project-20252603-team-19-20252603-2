package ui;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import ui.model.GameModel;

class GameModelTest {

    @Test
    void startGame_twoPlayers_gameStarted() {
        GameModel model = new GameModel();
        model.startGame(List.of("P0", "P1"));
        assertTrue(model.isGameStarted());
    }
}
