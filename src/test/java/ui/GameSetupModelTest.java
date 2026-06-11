package ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import domain.model.GameSetupModel;
import java.util.List;
import org.junit.jupiter.api.Test;

class GameSetupModelTest {

	private static final String DEFAULT_PREFIX = "Player";
	private static final String TOO_FEW_PLAYERS_KEY = "gameSetupModel.tooFewPlayers";
	private static final String TOO_MANY_PLAYERS_KEY = "gameSetupModel.tooManyPlayers";

	@Test
	void capturePlayerNamesFromInputs_listSizeOne_throwsIllegalArgumentException() {
		GameSetupModel model = new GameSetupModel();

		IllegalArgumentException ex = assertThrows(
				IllegalArgumentException.class,
				() -> model.capturePlayerNamesFromInputs(
						List.of("Vincent"), DEFAULT_PREFIX));
		assertEquals(TOO_FEW_PLAYERS_KEY, ex.getMessage());
	}

	@Test
	void capturePlayerNamesFromInputs_listSizeTwo_storesBothNames() {
		GameSetupModel model = new GameSetupModel();

		model.capturePlayerNamesFromInputs(
				List.of("Vincent", "Vinny"), DEFAULT_PREFIX);
		assertEquals(List.of("Vincent", "Vinny"), model.getPlayerNames());
	}

	@Test
	void capturePlayerNamesFromInputs_listSizeFive_storesAllNames() {
		GameSetupModel model = new GameSetupModel();

		model.capturePlayerNamesFromInputs(
				List.of("Vincent", "Vinny", "VV", "V", "vv"), DEFAULT_PREFIX);
		assertEquals(
				List.of("Vincent", "Vinny", "VV", "V", "vv"),
				model.getPlayerNames());
	}

	@Test
	void capturePlayerNamesFromInputs_listSizeSix_throwsIllegalArgumentException() {
		GameSetupModel model = new GameSetupModel();

		IllegalArgumentException ex = assertThrows(
				IllegalArgumentException.class,
				() -> model.capturePlayerNamesFromInputs(
						List.of("Vincent", "Vinny", "VV", "V", "vv", "v"),
						DEFAULT_PREFIX));
		assertEquals(TOO_MANY_PLAYERS_KEY, ex.getMessage());
	}

	@Test
	void capturePlayerNamesFromInputs_blankWhitespaceSecondName_usesDefaultPrefix() {
		GameSetupModel model = new GameSetupModel();

		model.capturePlayerNamesFromInputs(
				List.of("Vincent", "   "), DEFAULT_PREFIX);
		assertEquals(List.of("Vincent", "Player 2"), model.getPlayerNames());
	}

	@Test
	void capturePlayerNamesFromInputs_emptySecondName_usesDefaultPrefix() {
		GameSetupModel model = new GameSetupModel();

		model.capturePlayerNamesFromInputs(
				List.of("Vincent", ""), DEFAULT_PREFIX);
		assertEquals(List.of("Vincent", "Player 2"), model.getPlayerNames());
	}
}
