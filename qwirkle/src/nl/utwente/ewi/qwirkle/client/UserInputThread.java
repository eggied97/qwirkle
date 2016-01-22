package nl.utwente.ewi.qwirkle.client;

import java.util.Scanner;

import nl.utwente.ewi.qwirkle.callback.UserInputCallback;
import nl.utwente.ewi.qwirkle.model.enums.InputState;

public class UserInputThread extends Thread {

	private boolean running = true;
	private InputState state = InputState.FORLOGIN;
	private UserInputCallback callback;

	public UserInputThread(UserInputCallback callback) {
		this.callback = callback;
	}

	public void setRunning(Boolean b) {
		this.running = b;
	}
	
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
