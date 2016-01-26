package nl.utwente.ewi.qwirkle.server;

import java.util.ArrayList;
import java.util.List;

import nl.utwente.ewi.qwirkle.model.Bag;
import nl.utwente.ewi.qwirkle.model.Board;
import nl.utwente.ewi.qwirkle.model.Tile;
import nl.utwente.ewi.qwirkle.model.player.Player;
import nl.utwente.ewi.qwirkle.protocol.Protocol;
import nl.utwente.ewi.qwirkle.server.connect.ClientHandler;

public class Game {
	
	private Board board;
	private Bag bag;
	private List<ClientHandler> players;
	private int turn = 0;
	private boolean running = true;
	
	public Game(List<ClientHandler> players) {
		this.bag = new Bag();
		this.board = new Board();
		this.players = new ArrayList<>();
		this.players.addAll(players);
	}
	
	/**
	 * Give all players 6 tiles
	 */
	public void run() {
		for(ClientHandler ch : players) {
			List<Tile> newTiles = ch.getGame().getBag().getRandomTile(6);
			ch.getPlayer().bagToHand(newTiles);
			ch.sendMessage(Protocol.getInstance().serverDrawTile(newTiles));
		}
	}
	
	
	
	/**
	 * @return the running
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * @param running the running to set
	 */
	public void setRunning(boolean running) {
		this.running = running;
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
	
	public void setTurn(int turnToSet){
		this.turn = turnToSet;
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
	 * 
	 * Game end if:
	 * 	- no more possible moves with tiles on the board and in the players hand & deck
	 * 	- board is in perfect square
	 *  - one players hand is empty 
	 * 
	 * @return
	 */
	public boolean gameEnd() {
		
		System.err.println("game end check ");
		
		if(board.isSquare()) {
			System.err.println("board is square ");
			return true;
		}
		
		for(ClientHandler ch : players) {
			
			System.err.println("tiles in hand : " + ch.getPlayer().getHand().size());
			
			if(ch.getPlayer().getHand().size() == 0 ) {
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
