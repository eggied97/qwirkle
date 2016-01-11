package nl.utwente.ewi.qwirkle.model.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.awt.Point;
import nl.utwente.ewi.qwirkle.model.*;

public abstract class Player {
	
	private String name;
	private List<Tile> hand;
	
	public Player(String name) {
		this.name = name;
		this.hand = new ArrayList<>();
	}
	
	public String getName() {
		return name;
	}
	
	public abstract HashMap<Point, Tile> determineMove(Board board);
	
	public void makeMove(Board board) {
		HashMap<Point, Tile> move = determineMove(board);
		// TODO WAIT FOR CONFIRMATION FROM SERVER
	}
	
	public void bagToHand(List<Tile> tiles) {
		hand.addAll(tiles);
		// TODO FROM SERVER CALL THIS WITH A LIST OF TILES
	}
	
	
}
