package nl.utwente.ewi.qwirkle.model.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.awt.Point;
import nl.utwente.ewi.qwirkle.model.*;
import nl.utwente.ewi.qwirkle.model.player.strategy.SuperStrategy;

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

	/**
	 * called to determine which tiles a user wants to trade.
	 * 
	 * @return a <code> List </code> of
	 *         {@link nl.utwente.ewi.qwirkle.model.Tile}s which the player wants
	 *         to trade.
	 */
	public abstract List<Tile> determineTradeMove();

	/**
	 * called to determine which {@link nl.utwente.ewi.qwirkle.model.Move}s a
	 * player wants to do.
	 * 
	 * @return a <code> List </code> of
	 *         {@link nl.utwente.ewi.qwirkle.model.Move}s which the player wants
	 *         to do.
	 */
	public abstract List<Move> determinePutMove(Board board);

	/**
	 * determines what message the user wants to send.
	 * 
	 * @return the chatmessage
	 */
	public abstract String sendChat();

	/**
	 * needs to be called when entered a new game, so we reset the score & hand.
	 */
	public void newGame() {
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

	/**
	 * converts the string array to tiles, and then add those tiles.
	 * 
	 * @param tiles
	 */
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

	public int getLengthStreak() {
		List<Move> moves = new SuperStrategy().determineMove(new Board(), hand);

		return moves.size();
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
