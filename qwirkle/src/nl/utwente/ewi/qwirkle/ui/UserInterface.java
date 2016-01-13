package nl.utwente.ewi.qwirkle.ui;

import nl.utwente.ewi.qwirkle.model.player.Player;

public interface UserInterface {
	//Protocol
	public Player login(); //TODO maybe verander deze method name
	public int[] queueWithHowManyPlayers();
	public void changeTurn(Player p);
	public void playerTraded(Player p, int noOfTilesTraded);
	public void printMessage(String message);
	//Error handling
	public void showError(String message);
}
