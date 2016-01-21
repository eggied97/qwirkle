package nl.utwente.ewi.qwirkle.model.exceptions;

public class TooFewArgumentsException extends Exception {

	int p;

	public TooFewArgumentsException(int p) {
		this.p = p;
	}

	@Override
	public String getMessage() {
		return "error: there were too few arguments given (not according to protocol) :  " + this.p
				+ " arguments were given.";
	}
}