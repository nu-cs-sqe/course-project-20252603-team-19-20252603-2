package ui.navigation;

public class JavaFxScreenRouter implements ScreenRouter {

	private ScreenRouter navigation;

	public void configureNavigation(ScreenRouter navigation) {
		this.navigation = navigation;
	}

	@Override
	public void showStart() {
		navigation.showStart();
	}

	@Override
	public void showInstructions() {
		navigation.showInstructions();
	}

	@Override
	public void showGameSetup() {
		navigation.showGameSetup();
	}

	@Override
	public void showGame() {
		navigation.showGame();
	}

	@Override
	public void showWinner() {
		navigation.showWinner();
	}
}
