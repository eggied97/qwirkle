package nl.utwente.ewi.qwirkle.ui;

import java.util.List;
import java.util.Map;

import nl.utwente.ewi.qwirkle.callback.UserInterfaceCallback;
import nl.utwente.ewi.qwirkle.client.Game;
import nl.utwente.ewi.qwirkle.model.Tile;
import nl.utwente.ewi.qwirkle.model.player.Player;

public interface UserInterface {
	// Protocol
	/**
	 * Ask the user for information to connect with the <code> Server </code>
	 */
	public void askForServerInformation();
	
	/**
	 * Ask the user for a name
	 */
	public void askForLogin(); 

	/**
	 * Ask the user for the amount of players he wants to play
	 */
	public void askQueueWithHowManyPlayers();

	/**
	 * Change turn to the next <code> Player </code>
	 * @param p
	 */
	public void changeTurn(Player p);

	/**
	 * Show how many <code> Tiles </code> the  <code> Player </code> has traded 
	 * @param p
	 * @param noOfTilesTraded
	 */
	public void playerTraded(Player p, int noOfTilesTraded);

	/**
	 * Prints the message
	 * @param message
	 */
	public void printMessage(String message);

	/**
	 * Show the score to the user
	 * @param scoreMap
	 * @param fromGameEnd
	 */
	public void showScore(Map<Player, Integer> scoreMap, boolean fromGameEnd);

	/**
	 * Show the <code> Tiles </code> that the user owns
	 * @param tiles
	 */
	public void showHand(List<Tile> tiles);

	/**
	 * Ask the user if he wants to play or trade
	 */
	public void askForPlayOrExchange();

	/**
	 * Ask which <code> Move </code> the <code> Player </code> wants to make
	 */
	public void askForMove();

	/**
	 * Ask which <code> Tiles </code> the <code> Player </code> wants to trade
	 */
	public void askForTrade();
	
	/**
	 * Ask what message the user wants to send over chat
	 */
	public void askForChatMessage();
	
	/**
	 * Asks the user to enter the AI time
	 */
	public void askForAITime();
	
	/**
	 * disables the chat, if the server doesnt support it.
	 */
	public void disableChat();
	
	
	/**
	 * Set the callback
	 * @param callback
	 */
	public void setCallback(UserInterfaceCallback callback);
	
	

	// Error handling
	/**
	 * Show the error
	 * @param message
	 */
	public void showError(String message);

}
