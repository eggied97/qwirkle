package nl.utwente.ewi.qwirkle.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.utwente.ewi.qwirkle.client.connect.Client;
import nl.utwente.ewi.qwirkle.client.connect.resultCallback;
import nl.utwente.ewi.qwirkle.model.Board;
import nl.utwente.ewi.qwirkle.model.Move;
import nl.utwente.ewi.qwirkle.model.Point;
import nl.utwente.ewi.qwirkle.model.Tile;
import nl.utwente.ewi.qwirkle.model.exceptions.tooFewArgumentsException;
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
	boolean nextDrawNeedToRemoveTiles = false;
	private List<Tile> tilesThatNeedToBeRemoved;

	private Player turnPlayer;

	boolean isFirstRound = true;
	int turnCount = 0;

	/**
	 * 
	 * @param UI
	 *            The userinterface it uses
	 * @param players
	 *            the list of players in the game
	 * @param c
	 *            the <code>Client</code> it uses for comms with the server
	 * @param usingFeatures
	 *            List of <code> Features </code> that are enabled
	 */
	public Game(UserInterface UI, List<Player> players, Client c, List<IProtocol.Feature> usingFeatures) {
		this.board = new Board();

		this.players = players;
		this.UI = UI;
		this.c = c;

		// TODO check if this is fast enough to get the turn message
		c.setCallback(this);

		this.usingFeatures = usingFeatures;
	}

	public UserInterface getUI() {
		return this.UI;
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
		try {
			if (protocol.DEBUG) {
				System.out.println("Back from server (game)> " + result.trim());
			}

			String[] results = result.trim().split(" ");

			if (results.length == 0) {
				// TODO throw error
			}

			String command = results[0];
			String[] args = Arrays.copyOfRange(results, 1, results.length);

			switch (results[0]) {
			case IProtocol.SERVER_TURN:
				if (args.length != 1) {
					throw new tooFewArgumentsException(args.length);
				}

				handleTurnChange(args[0]);
				break;

			case IProtocol.SERVER_DRAWTILE:
				if (args.length == 0) {
					throw new tooFewArgumentsException(args.length);
				}

				if (nextDrawNeedToRemoveTiles) {
					if (turnPlayer instanceof HumanPlayer || turnPlayer instanceof ComputerPlayer) {
						turnPlayer.removeTilesFromHand(tilesThatNeedToBeRemoved);
					} else {
						// TODO is socket player -> throw error
					}
				}

				handleDrawTile(args);
				break;

			case IProtocol.SERVER_GAMEEND:
				if (args.length != players.size()) {
					throw new tooFewArgumentsException(args.length);
				}

				handleGameEnd(args);
				break;

			case IProtocol.SERVER_PASS:
				if (args.length != 1) {
					throw new tooFewArgumentsException(args.length);
				}

				handlePass(args[0]);
				break;

			case IProtocol.SERVER_MOVE_PUT:
				if (args.length < 1) {
					throw new tooFewArgumentsException(args.length);
				}

				handleMovePut(args);
				break;

			case IProtocol.SERVER_MOVE_TRADE:
				if (args.length < 1) {
					throw new tooFewArgumentsException(args.length);
				}

				handleMoveTrade(args);

				break;

			case IProtocol.SERVER_CHAT:
				if (args.length < 3) {
					throw new tooFewArgumentsException(args.length);
				}

				handleIncommingChatMessage(args);
				break;

			case IProtocol.SERVER_ERROR:
				if (args.length < 1) {
					throw new tooFewArgumentsException(args.length);
				}

				switch (IProtocol.Error.valueOf(args[0])) {
				case MOVE_INVALID:
					this.UI.showError("The move you made was invalid.");
					handleTurn(true);
					break;

				case MOVE_TILES_UNOWNED:
					this.UI.showError("The move you made was using tiles that you did not own.");
					handleTurn(true);
					break;

				case TRADE_FIRST_TURN:
					this.UI.showError("You wanted to trade on your first turn, you cant do this.");
					handleTurn(true);
					break;

				case INVALID_CHANNEL:
					this.UI.showError(
							"You wanted to send a message to a unknow channel. \n Usernames are uppercase specific , or use `global`.");
					handleTurn(true);
					break;

				default:
					break;
				}

				break;
			}
		} catch (tooFewArgumentsException e) {
			this.UI.showError("Something went bad with the protocol message : " + e.getMessage());
		}
	}

	private void handleIncommingChatMessage(String[] args) {

		Boolean isGlobal = args[0].equals("global");
		String sender = args[1];

		String message = Arrays.copyOfRange(args, 2, args.length).toString();

		// For change of color between private/global we use showError and
		// print message
		if (isGlobal) {
			this.UI.printMessage(sender + " > " + message);
		} else {
			this.UI.showError(sender + " > " + message);
		}

	}

	private void handleTurnChange(String name) {
		turnPlayer = getPlayerByName(name);

		handleTurn(false);
	}

	private void handleMoveTrade(String[] trades) {
		// TODO implement?

	}

	private void handleMovePut(String[] moves) {
		for (String m : moves) {
			String[] parts = m.split("@");

			if (parts.length != 2) {
				this.UI.showError("Invalid input");
			}

			String[] coord = parts[1].split(",");

			if (coord.length != 2) {
				this.UI.showError("Invalid input");
			}

			Point pResult = new Point(Integer.parseInt(coord[0]), Integer.parseInt(coord[1]));
			Move mResult = new Move(pResult, new Tile(Integer.parseInt(parts[0])));

			board.putTile(mResult);
		}
	}

	/**
	 * @param fromError
	 *            true if turn was replayed because of an error (Not count
	 *            towards the total turns...
	 */
	private void handleTurn(boolean fromError) {
		update();

		if (!fromError) {
			turnCount += 1;
		}

		if (isFirstRound && turnCount == players.size()) {
			isFirstRound = false;
		}

		if (turnPlayer instanceof HumanPlayer) {
			this.UI.printMessage("Turn changed, its your turn now");
			this.UI.printMessage(((HumanPlayer) turnPlayer).printHand());
			handlePlayerInput(((HumanPlayer) turnPlayer).determineMove(board));
		} else if (turnPlayer instanceof ComputerPlayer) {
			handlePlayerInput(((ComputerPlayer) turnPlayer).determineMove(board));
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

	private void handlePlayerInput(Object input) {

		if (input instanceof List<?>) {
			if (((List<?>) input).get(0) instanceof Move) {
				tilesThatNeedToBeRemoved = new ArrayList<>();

				for (Move m : (List<Move>) input) {
					tilesThatNeedToBeRemoved.add(m.getTile());
				}

				nextDrawNeedToRemoveTiles = true;

				c.sendMessage(protocol.getInstance().clientPutMove((List<Move>) input));
			} else if (((List<?>) input).get(0) instanceof Tile) {
				tilesThatNeedToBeRemoved = new ArrayList<>();
				tilesThatNeedToBeRemoved.addAll((List<Tile>) input);

				nextDrawNeedToRemoveTiles = true;

				c.sendMessage(protocol.getInstance().clientTradeMove(tilesThatNeedToBeRemoved));
			}
		} else if (input instanceof String) {
			// chat message
			String msg = (String) input;

			String[] msgs = msg.split(" ");
			String[] message = Arrays.copyOfRange(msgs, 1, msgs.length);

			c.sendMessage(protocol.getInstance().clientChat(msgs[0], message.toString()));
		}
	}

	private void handleDrawTile(String[] tiles) {
		for (Player p : players) {
			if (p instanceof HumanPlayer || p instanceof ComputerPlayer) {
				p.bagToHand(tiles);
			}
		}
	}

	private void handleGameEnd(String[] args) {
		Map<Player, Integer> scoreMap = new HashMap<>();

		for (String a : args) {
			String[] scoreNaam = a.split(",");

			if (scoreNaam.length != 2) {
				// TODO throw error
			}

			Player p = getPlayerByName(scoreNaam[1]);

			if (p != null) {
				scoreMap.put(p, Integer.parseInt(scoreNaam[0]));
			} else {
				// TODO throw error
			}
		}

		this.UI.showScore(scoreMap);

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
