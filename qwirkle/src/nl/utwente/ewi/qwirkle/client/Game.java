package nl.utwente.ewi.qwirkle.client;

import java.util.ArrayList;
import java.util.List;

import nl.utwente.ewi.qwirkle.model.Board;
import nl.utwente.ewi.qwirkle.model.player.Player;

public class Game {
	
	private Board board;
	private List<Player> players;
	
	public Game() {
		this.board = new Board();
		this.players = new ArrayList<>();
	}

}
