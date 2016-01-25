package nl.utwente.ewi.qwirkle.callback;

import java.util.List;

import nl.utwente.ewi.qwirkle.model.Move;
import nl.utwente.ewi.qwirkle.model.Tile;
import nl.utwente.ewi.qwirkle.model.player.Player;

/**
 * This callback is used when the user did something in the UI
 */
public interface UserInterfaceCallback {

	public void setupServer(String serverInformation);

	/**
	 * is called when the GUI logs someone in.
	 * 
	 * @param p
	 *            the generated <code> Player </code> instance
	 */
	public void login(Player p);
	
	public void setAITime(int time);

	/**
	 * is called when the GUI wants to join a queue.
	 * 
	 * @param queue
	 *            int array of queue's
	 */
	public void queue(int[] queue);

	/**
	 * determines what kind of action the user wants to do.
	 * 
	 * @param action
	 *            <code> p </code> for playing, <code> e</code> for trading,
	 *            <code> s</code> to see the scores, <code> c </code> to chat.
	 */
	public void determinedAction(String action);

	public void putMove(String unparsedString);

	public void putMove(List<Move> moves);

	public void putTrade(String unparsedString);

	public void putTrade(List<Tile> tiles);

	public void sendChat(String msg);
	
	public void printHint();
	
	public void quit();
}
