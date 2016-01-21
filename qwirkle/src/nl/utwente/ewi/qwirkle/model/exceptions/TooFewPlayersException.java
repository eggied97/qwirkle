package nl.utwente.ewi.qwirkle.model.exceptions;

public class TooFewPlayersException extends Exception {

	int p;

	public TooFewPlayersException(int p) {
		this.p = p;
	}

	@Override
	public String getMessage() {
		return "error: There are too few players, min players needed is 2, cuurent in game:  " + this.p;
	}
}