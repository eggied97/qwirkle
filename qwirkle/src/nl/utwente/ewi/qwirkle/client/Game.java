package nl.utwente.ewi.qwirkle.client;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import nl.utwente.ewi.qwirkle.client.connect.Client;
import nl.utwente.ewi.qwirkle.client.connect.resultCallback;
import nl.utwente.ewi.qwirkle.model.Board;
import nl.utwente.ewi.qwirkle.model.Move;
import nl.utwente.ewi.qwirkle.model.player.ComputerPlayer;
import nl.utwente.ewi.qwirkle.model.player.HumanPlayer;
import nl.utwente.ewi.qwirkle.model.player.Player;
import nl.utwente.ewi.qwirkle.protocol.IProtocol;
import nl.utwente.ewi.qwirkle.protocol.protocol;
import nl.utwente.ewi.qwirkle.ui.UserInterface;
import nl.utwente.ewi.qwirkle.ui.tui.TUIView;

public class Game implements resultCallback {

	private Board board;
	private List<Player> players;
	private UserInterface UI;
	private Client c;
	private List<IProtocol.Feature> usingFeatures;
	
	private Player turnPlayer;

	public Game(UserInterface UI, List<Player> players, Client c, List<IProtocol.Feature> usingFeatures) {
		this.board = new Board();

		this.players = players;
		this.UI = UI;
		this.c = c;

		// TODO check if this is fast enough to get the turn message
		c.setCallback(this);

		this.usingFeatures = usingFeatures;
	}

	private void checkFeatures() {
		// TODO implement
	}

	private void update() {
		if (UI instanceof TUIView) {
			this.UI.printMessage("Current board situation : ");
			this.UI.printMessage(board.toString());
		}
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

		case IProtocol.SERVER_PASS:
			if (args.length != 1) {
				// TODO throw error
			}

			handlePass(args[0]);
			break;

		case IProtocol.SERVER_ERROR:
			
			break;
		}
	}

	private void handleTurnChange(String name) {
		turnPlayer = getPlayerByName(name);		
		
		handleTurn();
	}
	
	private void handleTurn(){
		if (turnPlayer instanceof HumanPlayer) {
			this.UI.printMessage("Turn changed, its your turn now");
			handlePlayerMadeMove(((HumanPlayer) turnPlayer).determineMove(board));
		} else if (turnPlayer instanceof ComputerPlayer) {
			handlePlayerMadeMove(((ComputerPlayer) turnPlayer).determineMove(board));
		} else {
			this.UI.printMessage("Turn changed, its " + turnPlayer.getName() + " turn now");
		}
	}

	private void handlePass(String name) {
		Player p = getPlayerByName(name);

		if (p instanceof HumanPlayer) {
			this.UI.printMessage("You passed because there were no moves for you to be made");
		} else if (p instanceof ComputerPlayer) {
			this.UI.printMessage("This AI passed because there we no moves for him/her");
		} else {
			this.UI.printMessage("You passed because there were no moves for you to be made");
		}
	}

	private void handlePlayerMadeMove(List<Move> moves) {
		c.sendMessage(protocol.getInstance().clientPutMove(moves));
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
