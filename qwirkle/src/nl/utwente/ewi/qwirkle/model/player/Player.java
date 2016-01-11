package nl.utwente.ewi.qwirkle.model.player;

import java.util.HashMap;
import java.awt.Point;
import nl.utwente.ewi.qwirkle.model.*;

public abstract class Player {
	
	private String name;
	
	public Player(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public abstract HashMap<Point, Tile> determineMove(Board board);
	
	public void makeMove(Board board) {
		HashMap<Point, Tile> move = determineMove(board);
		// TODO WAIT FOR CONFIRMATION FROM SERVER
	}
	
	
}
