package nl.utwente.ewi.qwirkle.server;

import java.util.ArrayList;
import java.util.List;

import nl.utwente.ewi.qwirkle.model.Board;
import nl.utwente.ewi.qwirkle.model.player.Player;
import nl.utwente.ewi.qwirkle.server.model.Bag;

public class Game {
	
	private Board board;
	private Bag bag;
	private List<Player> players;
	
	public Game() {
		this.bag = new Bag();
		this.board = new Board();
		this.players = new ArrayList<>();
		// TODO PROTOCOL IMPLEMENTATION
	}
	
	/**
	 * 
	 * @return true if the <code> Player </code> is unable to perform any move
	 */
	public boolean noMoves() {
			return bag.isEmpty() || board.isSquare();
	}
	
}
