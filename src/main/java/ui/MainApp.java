package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.controller.StartController;
import ui.model.AppModel;
import ui.navigation.JavaFxScreenRouter;
import ui.navigation.ScreenRouter;
import ui.view.StartView;

public class MainApp extends Application {

	private static final int mainWindowWidth = 1000;
	private static final int mainWindowHeight = 800;
	private static final int mainWindowMinWidth = 600;
	private static final int mainWindowMinHeight = 500;

	@Override
	public void start(Stage primaryStage) {
		AppModel appModel = new AppModel();

		StartView startView = new StartView();

		Scene scene = new Scene(startView, mainWindowWidth, mainWindowHeight);

		JavaFxScreenRouter router = new JavaFxScreenRouter();
		router.configureNavigation(new ScreenRouter() {
			@Override
			public void showStart() {
				scene.setRoot(startView);
			}

			@Override
			public void showInstructions() {

			}

			@Override
			public void showGameSetup() {

			}

			@Override
			public void showGame() {

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
