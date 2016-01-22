package nl.utwente.ewi.qwirkle.callback;

import java.util.List;

import nl.utwente.ewi.qwirkle.model.Move;
import nl.utwente.ewi.qwirkle.model.Tile;
import nl.utwente.ewi.qwirkle.model.player.Player;

public interface UserInterfaceCallback {

	/**
	 * is called when the GUI logs someone in.
	 * 
	 * @param p
	 *            the generated <code> Player </code> instance
	 */
	public void login(Player p);

	/**
	 * is called when the GUI wants to join a queue.
	 * 
	 * @param queue
	 *            int array of queue's
	 */
	public void queue(int[] queue);
	
	public void determinedAction(String action);
	
	public void putMove(String unparsedString);
	public void putMove(List<Move> moves);
	
	public void putTrade(String unparsedString);
	public void putTrade(List<Tile> tiles);
	
	public void sendChat(String msg);
}
