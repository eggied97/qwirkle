package nl.utwente.ewi.qwirkle.server;

import java.util.ArrayList;
import java.util.List;

import nl.utwente.ewi.qwirkle.model.Board;
import nl.utwente.ewi.qwirkle.model.Tile;
import nl.utwente.ewi.qwirkle.model.player.Player;
import nl.utwente.ewi.qwirkle.protocol.protocol;
import nl.utwente.ewi.qwirkle.server.connect.ClientHandler;
import nl.utwente.ewi.qwirkle.server.model.Bag;

public class Game extends Thread {
	
	private Board board;
	private Bag bag;
	private List<ClientHandler> players;
	private int turn = 0;
	
	public Game(List<ClientHandler> players) {
		this.bag = new Bag();
		this.board = new Board();
		this.players = players;
		// TODO PROTOCOL IMPLEMENTATION
	}
	
	public void run() {
		for(ClientHandler ch : players) {
			List<Tile> newTiles = ch.getGame().getBag().getRandomTile(6);
			ch.getPlayer().bagToHand(newTiles);
			ch.sendMessage(protocol.getInstance().serverDrawTile(newTiles));
		}
	}
	
	public Player getPlayerTurn() {
		return players.get(turn).getPlayer();
	}
	
	public void nextTurn() {
		turn = (turn + 1) % players.size();
	}
	
	public boolean hasTurn(ClientHandler ch) {
		return players.indexOf(ch) == turn;
	}
	
	public boolean gameEnd() {
		if(!getBag().isEmpty()) {
			return false;
		}
		for(ClientHandler ch : players) {
			if(ch.getPlayer().getHand().isEmpty()) {
				return true;
			}
		}
		return false;
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
	
	public List<ClientHandler> getPlayers() {
		return this.players;
	}
	
}
