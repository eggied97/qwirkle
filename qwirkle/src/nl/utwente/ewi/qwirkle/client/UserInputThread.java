package nl.utwente.ewi.qwirkle.client;

import java.util.Scanner;

import nl.utwente.ewi.qwirkle.callback.UserInputCallback;
import nl.utwente.ewi.qwirkle.model.enums.InputState;

/**
 * This Thread handles the user input when using the TUI view.
 * 
 * This enables us to chat while it is not our turn.
 */

public class UserInputThread extends Thread {

	private boolean running = true;
	private InputState state = InputState.FORSERVERINFORMATION;
	private UserInputCallback callback;

	/**
	 * Constructor.
	 * 
	 * @param callback
	 *            the callback it uses to send the text to.
	 */
	public UserInputThread(UserInputCallback callback) {
		this.callback = callback;
	}

	/**
	 * sets the running state.
	 * 
	 * @param b
	 *            the running state
	 */
	public void setRunning(Boolean b) {
		this.running = b;
	}

	/**
	 * Sets the {@link nl.utwente.ewi.qwirkle.model.enums.InputState}, to
	 * determine the context.
	 * 
	 * @param s
	 *            the {@link nl.utwente.ewi.qwirkle.model.enums.InputState}
	 *            instance
	 */
	public void setInputState(InputState s) {
		this.state = s;
	}

	@Override
	public void run() {
		Scanner s = new Scanner(System.in);

		while (s.hasNextLine()) {
			String line = s.nextLine();
			if (this.callback != null) {
				this.callback.handlePlayerInput(line, this.state);
			}
		}
	}

}
