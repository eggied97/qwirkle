package nl.utwente.ewi.qwirkle.callback;

import nl.utwente.ewi.qwirkle.model.enums.InputState;

/**
 * This callback is used by the UserInputThread, so our multithreaded TUI can
 * talk back to the application.
 */
public interface UserInputCallback {

	/**
	 * called when the user typed something.
	 * 
	 * @param input
	 *            string of the input
	 * @param state
	 *            {@link nl.utwente.ewi.qwirkle.model.enums.InputState} the TUI
	 *            is in at this stage.
	 */
	public void handlePlayerInput(String input, InputState state);
}
