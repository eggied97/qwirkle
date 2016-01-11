package nl.utwente.ewi.qwirkle.model.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.awt.Point;
import nl.utwente.ewi.qwirkle.model.*;

public abstract class Player {
	
	private String name;
	private List<Tile> hand;
	
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
	 * Determinet the move to be played by the player
	 * @param board
	 * @return return a <code> Map </code> consisting of <code> Point </code> and <code> Tile </code> pairs 
	 */
	public abstract HashMap<Point, Tile> determineMove(Board board);
	
	
	/**
	 * Make the move that has been determined
	 * @param board
	 */
	public void makeMove(Board board) {
		HashMap<Point, Tile> move = determineMove(board);
		// TODO WAIT FOR CONFIRMATION FROM SERVER
	}
	
	/**
	 * Adds a list of tiles obtained from the bag to the hand of the player
	 * @param tiles
	 */
	public void bagToHand(List<Tile> tiles) {
		hand.addAll(tiles);
		// TODO FROM SERVER CALL THIS WITH A LIST OF TILES
	}
	
	
}
