package nl.utwente.ewi.qwirkle.model.exceptions;

public class PlayerNotInGameException extends Exception {
	String name;

	public PlayerNotInGameException(String name) {
		this.name = name;
	}

	@Override
	public String getMessage() {
		return "The user ( " + this.name + " ) is not in this game instance.";
	}
}
