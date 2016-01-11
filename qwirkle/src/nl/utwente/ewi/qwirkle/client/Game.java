package nl.utwente.ewi.qwirkle.client;

import java.util.ArrayList;
import java.util.List;

import nl.utwente.ewi.qwirkle.model.Board;
import nl.utwente.ewi.qwirkle.model.player.Player;
import nl.utwente.ewi.qwirkle.ui.UserInterface;
import nl.utwente.ewi.qwirkle.ui.tui.TUIView;

public class Game {
	
	private Board board;
	private List<Player> players;
	public UserInterface UI;
	
	public Game() {
		this.board = new Board();
		//TODO iets met protocol
		this.players = new ArrayList<>();
		this.UI = new TUIView();
	}

}
