package ui;

import domain.model.AppModel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.controller.GameController;
import ui.controller.GameSetupController;
import ui.controller.InstructionController;
import ui.controller.StartController;
import ui.controller.WinnerController;
import ui.navigation.JavaFxScreenRouter;
import ui.navigation.ScreenRouter;
import ui.view.GameSetupView;
import ui.view.GameView;
import ui.view.InstructionView;
import ui.view.StartView;
import ui.view.WinnerView;

public class MainApp extends Application {

	private static final int mainWindowWidth = 1000;
	private static final int mainWindowHeight = 800;
	private static final int mainWindowMinWidth = 600;
	private static final int mainWindowMinHeight = 500;

	@Override
	public void start(Stage primaryStage) {
		AppModel appModel = new AppModel();

		StartView startView = new StartView();
		InstructionView instructionView = new InstructionView();
		GameSetupView gameSetupView = new GameSetupView();
		GameView gameView = new GameView();
		WinnerView winnerView = new WinnerView();

		Scene scene = new Scene(startView, mainWindowWidth, mainWindowHeight);

		JavaFxScreenRouter router = new JavaFxScreenRouter();

		InstructionController instructionController = new InstructionController(
				instructionView, appModel, router
		);
		GameSetupController gameSetupController = new GameSetupController(
				gameSetupView, appModel, router
		);
		GameController gameController = new GameController(
				gameView, appModel, router
		);
		WinnerController winnerController = new WinnerController(
				winnerView, appModel, router
		);

		router.configureNavigation(new ScreenRouter() {
			@Override
			public void showStart() {
				scene.setRoot(startView);
			}

			@Override
			public void showInstructions() {
				instructionController.refreshView();
				scene.setRoot(instructionView);
			}

			@Override
			public void showGameSetup() {
				gameSetupController.refreshView();
				scene.setRoot(gameSetupView);
			}

			@Override
			public void showGame() {
				gameController.startGame();
				scene.setRoot(gameView);
			}

			@Override
			public void showWinner() {
				winnerController.refreshView();
				scene.setRoot(winnerView);
			}
		});

		new StartController(startView, appModel, router);

		primaryStage.setTitle("Exploding Kittens");
		primaryStage.setScene(scene);

		primaryStage.setMinWidth(mainWindowMinWidth);
		primaryStage.setMinHeight(mainWindowMinHeight);

		primaryStage.show();
	}

	public static void main(String[] args) {
		launch();
	}
}
