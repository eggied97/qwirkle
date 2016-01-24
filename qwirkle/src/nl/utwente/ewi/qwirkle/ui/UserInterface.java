package nl.utwente.ewi.qwirkle.ui;

import java.util.List;
import java.util.Map;

import nl.utwente.ewi.qwirkle.callback.UserInterfaceCallback;
import nl.utwente.ewi.qwirkle.client.Game;
import nl.utwente.ewi.qwirkle.model.Tile;
import nl.utwente.ewi.qwirkle.model.player.Player;

public interface UserInterface {
	// Protocol
	public void askForServerInformation();
	
	public void askForLogin(); // TODO maybe verander deze method name

	public void askQueueWithHowManyPlayers();

	public void changeTurn(Player p);

	public void playerTraded(Player p, int noOfTilesTraded);

	public void printMessage(String message);

	public void showScore(Map<Player, Integer> scoreMap, boolean fromGameEnd);

	public void showHand(List<Tile> tiles);

	public void askForPlayOrExchange();

	public void askForMove();

	public void askForTrade();
	
	public void askForChatMessage();
	
	public void setCallback(UserInterfaceCallback callback);
	
	

	// Error handling
	public void showError(String message);

}
