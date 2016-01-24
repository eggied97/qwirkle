package nl.utwente.ewi.qwirkle.model.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.awt.Point;
import nl.utwente.ewi.qwirkle.model.*;

public abstract class Player {

	private String name;
	private List<Tile> hand;
	private int score = 0;

	/**
	 * 
	 * @param name
	 */
	public Player(String name) {
		this.name = name;
		this.hand = new ArrayList<>();
	}

	/**
	 * 
	 * @return return the <code> name </code> of the player
	 */
	public String getName() {
		return name;
	}

	/**
	 * Determine the move to be played by the player.
	 * 
	 * @param board
	 * @return return a <code> Map </code> consisting of <code> Point </code>
	 *         and <code> Tile </code> pairs if it is an move \n return
	 *         <code> List</code> of tiles when it is a trade
	 */
	public abstract String determineAction();

	public abstract List<Tile> determineTradeMove();

	public abstract List<Move> determinePutMove(Board board);

	public abstract String sendChat();

	public void newGame(){
		this.hand.clear();
		this.score = 0;
	}

	/**
	 * Adds a list of tiles obtained from the bag to the hand of the player.
	 * 
	 * @param tiles
	 */
	public void bagToHand(List<Tile> tiles) {
		hand.addAll(tiles);
	}

	public void bagToHand(String[] tiles) {
		List<Tile> lTiles = new ArrayList<>();

		for (String t : tiles) {
			Tile f = new Tile(Integer.parseInt(t));
			lTiles.add(f);
		}

		bagToHand(lTiles);
	}

	/**
	 * removes the list of tiles from the players hand (after a trade / move).
	 * 
	 * @param tiles
	 *            that need to be removed
	 */
	public void removeTilesFromHand(List<Tile> tiles) {
		for (Tile t : tiles) {
			this.hand.remove(t);
		}
	}

	/**
	 * @return the hand of the player
	 */
	public List<Tile> getHand() {
		return this.hand;
	}

	/**
	 * adds the score of the move to the total score.
	 * 
	 * @param score
	 *            of the move
	 */
	public void addScore(int score) {
		this.score += score;
	}

	/**
	 * @return the score of the player
	 */
	public int getScore() {
		return score;
	}
}
