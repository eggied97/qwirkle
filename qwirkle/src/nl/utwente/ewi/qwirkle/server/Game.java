package nl.utwente.ewi.qwirkle.server;

import java.util.ArrayList;
import java.util.List;

import nl.utwente.ewi.qwirkle.model.Board;
import nl.utwente.ewi.qwirkle.model.player.Player;
import nl.utwente.ewi.qwirkle.server.model.Bag;

public class Game extends Thread {
	
	private Board board;
	private Bag bag;
	private List<Player> players;
	
	public Game(List<Player> players) {
		this.bag = new Bag();
		this.board = new Board();
		this.players = players;
		// TODO PROTOCOL IMPLEMENTATION
	}
	
	public void run() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return true if the <code> Player </code> is unable to perform any move
	 */
	public boolean noMoves() {
			return bag.isEmpty() || board.isSquare();
	}
	
	public Bag getBag() {
		return this.bag;
	}
	
	public Board getBoard() {
		return this.board;
	}
	
	public List<Player> getPlayers() {
		return this.players;
	}
	
}
