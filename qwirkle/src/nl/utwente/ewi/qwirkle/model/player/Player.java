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
	 * Determine the move to be played by the player
	 * @param board
	 * @return return a <code> Map </code> consisting of <code> Point </code> and <code> Tile </code> pairs 
	 */
	public abstract List<Move> determineMove(Board board);
	
	
	/**
	 * Make the move that has been determined
	 * @param board
	 */
	public void makeMove(Board board) {
		List<Move> moves = determineMove(board);
		// TODO WAIT FOR CONFIRMATION FROM SERVER
		if(true){
			board.putTile(moves);
		}
	}
	
	/**
	 * Adds a list of tiles obtained from the bag to the hand of the player
	 * @param tiles
	 */
	public void bagToHand(List<Tile> tiles) {
		hand.addAll(tiles);
	}
	
	public void bagToHand(String[] tiles){
		List<Tile> lTiles = new ArrayList<>();
		
		for(String t : tiles){
			Tile f = new Tile(Integer.parseInt(t));
			lTiles.add(f);
		}
		
		bagToHand(lTiles);
	}
	
	public List<Tile> getHand() {
		return this.hand;
	}
	
	public void addScore(int score) {
		this.score += score;
	}
	
	public int getScore() {
		return score;
	}
	
	
	
}
