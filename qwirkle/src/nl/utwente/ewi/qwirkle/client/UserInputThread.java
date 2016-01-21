package nl.utwente.ewi.qwirkle.client;

import java.util.Scanner;


import nl.utwente.ewi.qwirkle.model.enums.InputState;

public class UserInputThread extends Thread {

	private Game g;
	private boolean running = true;
	private InputState state = InputState.IDLE;

	public UserInputThread(Game g) {
		this.g = g;
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
			if (this.g != null) {
				this.g.handlePlayerInput(line, this.state);
			}
		}
	}

}
