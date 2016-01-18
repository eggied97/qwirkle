package nl.utwente.ewi.qwirkle.ui;

import java.util.List;
import java.util.Map;

import nl.utwente.ewi.qwirkle.client.Game;
import nl.utwente.ewi.qwirkle.model.Tile;
import nl.utwente.ewi.qwirkle.model.player.Player;

public interface UserInterface {
	// Protocol
	public Player login(); // TODO maybe verander deze method name

	public int[] queueWithHowManyPlayers();

	public void changeTurn(Player p);

	public void playerTraded(Player p, int noOfTilesTraded);

	public void printMessage(String message);

	public void showScore(Map<Player, Integer> scoreMap);

	public void showHand(List<Tile> tiles);

	public String askForPlayOrExchange();

	public String askForMove();

	public String askForTrade();
	
	public String askForChatMessage();
	
	

	// Error handling
	public void showError(String message);

}
