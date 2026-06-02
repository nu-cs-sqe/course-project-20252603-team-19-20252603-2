package ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;
import ui.model.GameSetupModel;

class GameSetupModelTest {

	private static final String DEFAULT_PREFIX = "Player";
	private static final String TOO_FEW_PLAYERS_KEY = "gameSetupModel.tooFewPlayers";

	@Test
	void capturePlayerNamesFromInputs_listSizeOne_throwsIllegalArgumentException() {
		GameSetupModel model = new GameSetupModel();

		IllegalArgumentException ex = assertThrows(
				IllegalArgumentException.class,
				() -> model.capturePlayerNamesFromInputs(
						List.of("Vincent"), DEFAULT_PREFIX));
		assertEquals(TOO_FEW_PLAYERS_KEY, ex.getMessage());
	}
}
