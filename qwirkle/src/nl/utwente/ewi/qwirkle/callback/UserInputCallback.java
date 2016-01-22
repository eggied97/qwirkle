package nl.utwente.ewi.qwirkle.callback;

import nl.utwente.ewi.qwirkle.model.enums.InputState;

public interface UserInputCallback {
	public void handlePlayerInput(String input, InputState state);
}
