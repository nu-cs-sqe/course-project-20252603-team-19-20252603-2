package ui.controller;

import domain.model.AppModel;
import ui.navigation.ScreenRouter;
import ui.view.InstructionView;

public class InstructionController {
	private final Runnable refreshAction;

	public InstructionController(
			InstructionView view, AppModel appModel, ScreenRouter router
	) {
		this.refreshAction = () -> view.updateDisplay(appModel.getResourceBundle());
		view.setOnBackAction(router::showStart);
		refreshAction.run();
	}

	public void refreshView() {
		refreshAction.run();
	}
}
