package nl.utwente.ewi.qwirkle.client;

import java.util.Scanner;


import nl.utwente.ewi.qwirkle.model.enums.inputState;

public class userInputThread extends Thread {

	private Game g;
	private boolean running = true;
	private inputState state = inputState.IDLE;

	public userInputThread(Game g) {
		this.g = g;
	}

	public void setRunning(Boolean b) {
		this.running = b;
	}
	
	public void setInputState(inputState s){
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
