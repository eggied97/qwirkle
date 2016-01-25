package nl.utwente.ewi.qwirkle.model.exceptions;

public class InvalidServerResponseException extends Exception {

	@Override
	public String getMessage() {
		return "This server message is invalid";
	}

}
