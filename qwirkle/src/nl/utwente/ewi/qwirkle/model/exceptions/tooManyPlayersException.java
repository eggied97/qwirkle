package nl.utwente.ewi.qwirkle.model.exceptions;

import java.awt.Point;

public class tooManyPlayersException extends Exception {

	int p;

	public tooManyPlayersException(int p) {
		this.p = p;
	}

	@Override
	public String getMessage() {
		return "error: There are too many players, max allowed is 4, cuurent in game:  " + this.p;
	}
}