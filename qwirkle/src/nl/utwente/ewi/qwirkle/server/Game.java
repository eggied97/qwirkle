package nl.utwente.ewi.qwirkle.server;

import java.util.ArrayList;
import java.util.List;

import nl.utwente.ewi.qwirkle.model.Board;
import nl.utwente.ewi.qwirkle.model.Tile;
import nl.utwente.ewi.qwirkle.model.player.Player;
import nl.utwente.ewi.qwirkle.protocol.protocol;
import nl.utwente.ewi.qwirkle.server.connect.ClientHandler;
import nl.utwente.ewi.qwirkle.server.model.Bag;

public class Game {
	
	private Board board;
	private Bag bag;
	private List<ClientHandler> players;
	private int turn = 0;
	
	public Game(List<ClientHandler> players) {
		this.bag = new Bag();
		this.board = new Board();
		this.players = new ArrayList<>();
		this.players.addAll(players);
		// TODO PROTOCOL IMPLEMENTATION <- Still needed -Egbert
	}
	
	/**
	 * Give all players 6 tiles
	 */
	public void run() {
		for(ClientHandler ch : players) {
			List<Tile> newTiles = ch.getGame().getBag().getRandomTile(6);
			ch.getPlayer().bagToHand(newTiles);
			ch.sendMessage(protocol.getInstance().serverDrawTile(newTiles));
		}
	}
	
	/**
	 * Returns the player that holds the turn
	 * @return
	 */
	public Player getPlayerTurn() {
		return players.get(turn).getPlayer();
	}
	
	/**
	 * Increment the turn 
	 */
	public void nextTurn() {
		turn = (turn + 1) % players.size();
	}
	
	/**
	 * Returns true when the <code> ClientHandler </code> holds the turn
	 * @param ch
	 * @return
	 */
	public boolean hasTurn(ClientHandler ch) {
		return players.indexOf(ch) == turn;
	}
	
	/**
	 * Returns true when one of the factors of game end has been met
	 * @return
	 */
	public boolean gameEnd() {
		if(getBag().isEmpty() || board.isSquare()) {
			return true;
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
	
	/**
	 * Returns the bag
	 * @return
	 */
	public Bag getBag() {
		return this.bag;
	}
	
	/**
	 * Returns the board
	 * @return
	 */
	public Board getBoard() {
		return this.board;
	}
	
	/**
	 * Returns the list of players
	 * @return
	 */
	public List<ClientHandler> getPlayers() {
		return this.players;
	}
	
}
