package nl.utwente.ewi.qwirkle.client;

import java.util.Arrays;
import java.util.List;

import nl.utwente.ewi.qwirkle.client.connect.Client;
import nl.utwente.ewi.qwirkle.client.connect.resultCallback;
import nl.utwente.ewi.qwirkle.model.Board;
import nl.utwente.ewi.qwirkle.model.player.ComputerPlayer;
import nl.utwente.ewi.qwirkle.model.player.HumanPlayer;
import nl.utwente.ewi.qwirkle.model.player.Player;
import nl.utwente.ewi.qwirkle.protocol.IProtocol;
import nl.utwente.ewi.qwirkle.ui.UserInterface;

public class Game implements resultCallback {

	private Board board;
	private List<Player> players;
	public UserInterface UI;
	private Client c;

	public Game(UserInterface UI, List<Player> players, Client c) {
		this.board = new Board();

		this.players = players;
		this.UI = UI;
		this.c = c;

		// TODO check if this is fast enough to get the turn message
		c.setCallback(this);
	}

	private void update() {
		System.out.println("Current board situation : ");
		System.out.println(board.toString());
	}

	@Override
	public void resultFromServer(String result) {
		String[] results = result.split(" ");

		if (results.length == 0) {
			// TODO throw error
		}

		String command = results[0];
		String[] args = Arrays.copyOfRange(results, 1, results.length);

		switch (results[0]) {
		case IProtocol.SERVER_TURN:
			if (args.length != 1) {
				// TODO throw error
			}
			
			handleTurnChange(args[0]);
			break;
		}
	}

	private void handleTurnChange(String name) {
		Player p = getPlayerByName(name);

		if (p instanceof HumanPlayer) {
			System.out.println("Turn changed, its your turn now");
			((HumanPlayer) p).determineMove(board);
		} else if (p instanceof ComputerPlayer) {
			((ComputerPlayer) p).determineMove(board);
		} else {
			System.out.println("Turn changed, its " + name + " turn now");
		}
	}

	private Player getPlayerByName(String name) {
		for (Player p : players) {
			if (p.getName().equals(name)) {
				return p;
			}
		}

		return null;
	}

}
