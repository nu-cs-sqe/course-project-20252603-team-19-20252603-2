package ui.controller;

import java.util.function.IntConsumer;
import ui.model.AppModel;
import ui.navigation.ScreenRouter;
import ui.view.GameSetupView;

/**
 * Controller class coordinating events between GameSetupModel and GameSetupView.
 */
public class GameSetupController {

	private static final class SetupViewContext {
		private final GameSetupView view;
		private final AppModel appModel;
		private final IntConsumer playerCountHandler;

		SetupViewContext(
				GameSetupView view,
				AppModel appModel,
				IntConsumer playerCountHandler
		) {
			this.view = view;
			this.appModel = appModel;
			this.playerCountHandler = playerCountHandler;
		}

		GameSetupView getView() {
			return view;
		}

		AppModel getAppModel() {
			return appModel;
		}

		IntConsumer getPlayerCountHandler() {
			return playerCountHandler;
		}
	}

	private SetupViewContext setupContext;
	private final GameController[] gameControllerRef;

	private static final int defaultHeightChange = 75;

	public GameSetupController(
			GameSetupView view, AppModel appModel, ScreenRouter router
	) {
		this.gameControllerRef = new GameController[1];
		this.setupContext = createSetupContext(view, appModel);

		setupContext.getView().setOnBackAction(
				router::showStart
		);
		setupContext.getView().setOnLaunchAction(() -> {
			String defaultPrefix = setupContext
					.getAppModel()
					.getResourceBundle()
					.getString("gameSetupView.defaultName");

			setupContext
					.getAppModel()
					.capturePlayerNamesFromInputs(
							setupContext
									.getView()
									.getRawPlayerNameInputs(),
							defaultPrefix
					);

			if (gameControllerRef[0] != null) {
				gameControllerRef[0].startGame();
			}

			router.showGame();
		});

		refreshSetupView(setupContext);
	}

	public void configureLaunch(GameController gameController) {
		gameControllerRef[0] = gameController;
	}

	public void refreshView() {
		this.setupContext = createSetupContext(
				setupContext.getView(), setupContext.getAppModel()
		);
		refreshSetupView(setupContext);
	}

	private static SetupViewContext createSetupContext(
			GameSetupView view, AppModel appModel
	) {
		final SetupViewContext[] contextRef = new SetupViewContext[1];
		final IntConsumer[] handlerRef = new IntConsumer[1];
		handlerRef[0] = selection ->
				handlePlayerCountSelection(
						contextRef[0],
						selection
				);
		contextRef[0] = new SetupViewContext(view, appModel, handlerRef[0]);
		return contextRef[0];
	}

	private static void handlePlayerCountSelection(
			SetupViewContext context, int selection
	) {
		int playerDifference = selection - context.getAppModel().getNumberPlayer();
		int heightChange = defaultHeightChange * playerDifference;
		context.getView().updateSetupContainerHeight(heightChange);
		context.getAppModel().setNumberPlayer(selection);
		refreshSetupView(context);
	}

	private static void refreshSetupView(SetupViewContext context) {
		context.getView().updateDisplay(context.getAppModel().getResourceBundle());
		context.getView().updatePlayerCountButtons(
				context.getAppModel().getNumberPlayer(),
				context.getPlayerCountHandler()
		);
		context.getView().updatePlayerNameSection(
				context.getAppModel().getNumberPlayer(),
				context.getAppModel().getResourceBundle()
		);
	}
}
