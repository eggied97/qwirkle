package nl.utwente.ewi.qwirkle.model.enums;

/**
 * Used in the {@link nl.utwente.ewi.qwirkle.client.UserInputThread} to
 * determine the context of the input from the user
 * 
 */
public enum InputState {
	FORSERVERINFORMATION, FORLOGIN, FORASKINGAITIME, FORQUEUE, IDLE, FORMOVE, FORTRADE, FORCHAT;
}
