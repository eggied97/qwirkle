package nl.utwente.ewi.qwirkle.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

import nl.utwente.ewi.qwirkle.callback.ResultCallback;
import nl.utwente.ewi.qwirkle.callback.UserInputCallback;
import nl.utwente.ewi.qwirkle.callback.UserInterfaceCallback;
import nl.utwente.ewi.qwirkle.client.connect.Client;
import nl.utwente.ewi.qwirkle.model.Board;
import nl.utwente.ewi.qwirkle.model.Move;
import nl.utwente.ewi.qwirkle.model.Point;
import nl.utwente.ewi.qwirkle.model.Tile;
import nl.utwente.ewi.qwirkle.model.enums.Color;
import nl.utwente.ewi.qwirkle.model.enums.InputState;
import nl.utwente.ewi.qwirkle.model.exceptions.TooFewArgumentsException;
import nl.utwente.ewi.qwirkle.model.player.ComputerPlayer;
import nl.utwente.ewi.qwirkle.model.player.HumanPlayer;
import nl.utwente.ewi.qwirkle.model.player.Player;
import nl.utwente.ewi.qwirkle.protocol.IProtocol;
import nl.utwente.ewi.qwirkle.protocol.protocol;
import nl.utwente.ewi.qwirkle.ui.UserInterface;
import nl.utwente.ewi.qwirkle.ui.gui.GUIView;
import nl.utwente.ewi.qwirkle.ui.gui.ProtocolControl;
import nl.utwente.ewi.qwirkle.ui.tui.TUIView;

public class Game implements ResultCallback, UserInterfaceCallback {

	private Board board;
	private List<Player> players;
	private UserInterface UI;
	private Client c;
	private List<IProtocol.Feature> usingFeatures;
	boolean nextDrawNeedToRemoveTiles = false;
	private List<Tile> tilesThatNeedToBeRemoved;
	private ProtocolControl control;
	private Player turnPlayer;

	Map<IProtocol.Feature, Boolean> featuresEnabled;

	boolean isFirstRound = true;
	int turnCount = 0;

	boolean playing = true;


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
	public Game(UserInterface UI, List<Player> players, Client c, List<IProtocol.Feature> serverFeatures) {
		this.board = new Board();

		this.players = players;
		this.UI = UI;
		this.c = c;

		
		if (UI instanceof GUIView) {
			control = new ProtocolControl(UI, this);
			((GUIView) UI).changeFrame();
		} else {
			this.UI.askForPlayOrExchange();
		}

		// TODO check if this is fast enough to get the turn message
		c.setCallback(this);

		featuresEnabled = new HashMap<>();
		checkFeatures(serverFeatures);

		this.usingFeatures = serverFeatures;
	}

	public Player getTurnPlayer() {
		return turnPlayer;
	}
	
	public Client getClient() {
		return this.c;
	}

	/**
	 * 
	 * @return the UserInterface instance that is used
	 */
	public UserInterface getUI() {
		return this.UI;
	}

	private void checkFeatures(List<IProtocol.Feature> usingFeatures) {
		for(IProtocol.Feature f : IProtocol.Feature.values()){
			featuresEnabled.put(f, false);
		}
		
		for(IProtocol.Feature f : usingFeatures){
			featuresEnabled.put(f, true);
		}
	}

	/**
	 * Prints the current state of the board on the UI instance.
	 */
	private void update() {
		if (UI instanceof TUIView) {
			this.UI.printMessage("Current board situation : ");
			this.UI.printMessage(board.toString());
		} else {
			((GUIView) this.UI).updateBoard();
			((GUIView) this.UI).showScore(players);
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
			} // TODO throw error

			String command = results[0];
			String[] args = Arrays.copyOfRange(results, 1, results.length);

			switch (results[0]) {
				case IProtocol.SERVER_TURN:
					if (args.length != 1) {
						throw new TooFewArgumentsException(args.length);
					}
	
					handleTurnChange(args[0]);
					break;
	
				case IProtocol.SERVER_DRAWTILE:
	
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
						throw new TooFewArgumentsException(args.length);
					}
	
					handleGameEnd(args);
					break;
	
				case IProtocol.SERVER_PASS:
					if (args.length != 1) {
						throw new TooFewArgumentsException(args.length);
					}
	
					handlePass(args[0]);
					break;
	
				case IProtocol.SERVER_MOVE_PUT:
					if (args.length < 1) {
						throw new TooFewArgumentsException(args.length);
					}
	
					handleMovePut(args);
					break;
	
				case IProtocol.SERVER_MOVE_TRADE:
					if (args.length < 1) {
						throw new TooFewArgumentsException(args.length);
					}
	
					handleMoveTradeFromOpponent(args);
	
					break;
	
				case IProtocol.SERVER_CHAT:
					if (args.length < 3) {
						throw new TooFewArgumentsException(args.length);
					}
	
					handleIncommingChatMessage(args);
					break;
	
				case IProtocol.SERVER_ERROR:
					//TODO  INVALID_CHANNEL wordt niet gestuurt vanuit server :/
					if (args.length < 1) {
						throw new TooFewArgumentsException(args.length);
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
		} catch (TooFewArgumentsException e) {
			this.UI.showError("Something went bad with the protocol message : " + e.getMessage());
		}
	}

	/**
	 * handles an incomming chat message.
	 * 
	 * @param args
	 */
	private void handleIncommingChatMessage(String[] args) {

		Boolean isGlobal = args[0].equals("global");
		String sender = args[1];

		// only handle this if we ddidnt send this
		if (!(getPlayerByName(sender) instanceof HumanPlayer)) {

			String message = Arrays.copyOfRange(args, 2, args.length).toString();
			// For change of color between private/global we use showError and
			// print message
			if (this.UI instanceof TUIView) {
				if (isGlobal) {
					this.UI.printMessage(sender + " > " + message);
				} else {
					this.UI.showError(sender + " > " + message);
				}

				if (getPlayerByName(sender) instanceof HumanPlayer) {
					this.UI.askForPlayOrExchange();
				}
			} else {

				StringBuilder builder = new StringBuilder();
				for (String s : Arrays.copyOfRange(args, 2, args.length)) {
					builder.append(s + " ");
				}

				if (isGlobal) {
					Style s = ((GUIView) this.UI).getFrame().getTextArea().addStyle("Style", null);
					StyleConstants.setForeground((MutableAttributeSet) s, java.awt.Color.BLUE);
					((GUIView) this.UI).setChat(sender + " > " + builder.toString(), s);
				} else {
					Style s = ((GUIView) this.UI).getFrame().getTextArea().addStyle("Style", null);
					StyleConstants.setForeground((MutableAttributeSet) s, java.awt.Color.MAGENTA);
					((GUIView) this.UI).setChat(sender + " > " + builder.toString(), s);
				}
			}
		}
	}

	/**
	 * handles an turn change.
	 * 
	 * @param name
	 *            - name of this turn's player
	 */
	private void handleTurnChange(String name) {
		turnPlayer = getPlayerByName(name);

		handleTurn(false);
	}

	/**
	 * handles an trade that a player made.
	 * 
	 * @param trades
	 *            - number of tiles traded
	 */
	private void handleMoveTradeFromOpponent(String[] trades) {
		this.UI.printMessage(turnPlayer.getName() + " just traded " + trades[0] + " tiles.");
	}

	/**
	 * handles an move a player made (and put it in the
	 * {@link nl.utwente.ewi.qwirkle.model.Board}).
	 * 
	 * @param moves
	 *            - String representation of the move set
	 */
	private void handleMovePut(String[] moves) {
		List<Move> aMoves = new ArrayList<>();

		for (String m : moves) {
			String[] parts = m.split("@");

			if (parts.length != 2) {
				this.UI.showError("Invalid input");
			}

			String[] coord = parts[1].split(",");

			if (coord.length != 2) {
				this.UI.showError("Invalid input");
			}

			try {
				Point pResult = new Point(Integer.parseInt(coord[0]), Integer.parseInt(coord[1]));
				Move mResult = new Move(pResult, new Tile(Integer.parseInt(parts[0])));
				aMoves.add(mResult);
			} catch (NumberFormatException e) {
				this.UI.showError("Enter a valid coordinates and/or tiles");
				handleTurn(false);
			}
		}

		int score = board.putTile(aMoves);
		turnPlayer.addScore(score);
	}

	/**
	 * Is public because we need to be able to call it from humanplayer (on b).
	 * 
	 * @param fromError
	 *            true if turn was replayed because of an error (Not count
	 *            towards the total turns...
	 */
	public void handleTurn(boolean fromError) {
		update();

		if (!fromError) {
			turnCount += 1;
		}

		if (isFirstRound && turnCount == players.size()) {
			isFirstRound = false;
		}

		if (turnPlayer instanceof HumanPlayer) {

			if (this.UI instanceof TUIView) {
				this.UI.printMessage("Turn changed, its your turn now");
				this.UI.showHand(turnPlayer.getHand());
				
				this.UI.askForPlayOrExchange();
			} else {
				this.UI.printMessage("It's your turn now");
				this.UI.showHand(turnPlayer.getHand());
			}

		} else if (turnPlayer instanceof ComputerPlayer) {
			handleTurnPcPlayer();
		} else {
			this.UI.printMessage("Turn changed, its " + turnPlayer.getName() + " turn now");
		}
	}

	private void handleTurnPcPlayer() {
		// if the moves.size == 0 => do trade.

		List<Move> moves = turnPlayer.determinePutMove(board);

		if (moves.size() == 0) {
			List<Tile> tiles = turnPlayer.determineTradeMove();

			tilesThatNeedToBeRemoved = new ArrayList<>();
			tilesThatNeedToBeRemoved.addAll(tiles);

			nextDrawNeedToRemoveTiles = true;

			c.sendMessage(protocol.getInstance().clientTradeMove(tilesThatNeedToBeRemoved));
		} else {
			tilesThatNeedToBeRemoved = new ArrayList<>();

			for (Move m : moves) {
				tilesThatNeedToBeRemoved.add(m.getTile());
			}

			nextDrawNeedToRemoveTiles = true;

			c.sendMessage(protocol.getInstance().clientPutMove(moves));
		}
	}

	/**
	 * handles an pass because the player could not do anything.
	 * 
	 * @param name
	 *            - name of the player that has passed
	 */
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
	
	/**
	 * handles when the user gets new tiles.
	 * 
	 * @param tiles
	 *            - the tiles recieved
	 */
	private void handleDrawTile(String[] tiles) {
		System.err.println("drawTile ");
		for (Player p : players) {
			if (p instanceof HumanPlayer || p instanceof ComputerPlayer) {
				p.bagToHand(tiles);
				if (this.UI instanceof GUIView) {
					this.UI.showHand(p.getHand());
				}
			}
		}
		
		this.UI.askForPlayOrExchange();
	}

	/**
	 * handles the situation that a game has ended.
	 * 
	 * @param args
	 *            - name of players with the score
	 */
	private void handleGameEnd(String[] args) {
		Map<Player, Integer> scoreMap = new HashMap<>();

		String errorOrWin = args[0];

		String[] newArgs = Arrays.copyOfRange(args, 1, args.length);

		for (String a : newArgs) {
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

		this.UI.printMessage("Win by " + (errorOrWin.equals("WIN") ? "win" : "error") + " :");
		this.UI.showScore(scoreMap);

		playing = false;

	}

	/**
	 * 
	 * @param name
	 *            - name of player we need to find
	 * @return {@link nl.utwente.ewi.qwirkle.model.player} instance which
	 *         corresponds with the name
	 */
	private Player getPlayerByName(String name) {
		for (Player p : players) {
			if (p.getName().equals(name)) {
				return p;
			}
		}

		return null;
	}

	@Override
	public void login(Player p) {} //not called

	@Override
	public void queue(int[] queue) {} //not called

	@Override
	public void determinedAction(String action) {
		switch (action) {
			case "p":				
				if (turnPlayer instanceof HumanPlayer) {
					this.UI.askForMove();
				} else {
					this.UI.showError("Wait for your turn");
				}
	
				break;
			case "e":
				if (turnPlayer instanceof HumanPlayer ) {
					this.UI.askForTrade();
				} else {
					this.UI.showError("Wait for your turn");
				}
	
				break;
			case "c":	
				this.UI.askForChatMessage();
	
				break;
			default:
				this.UI.showError("Wrong argument.");
				handleTurn(true);
				break;
		}
	}
	


	@Override
	public void putMove(String unparsedString) {
		if ( turnPlayer instanceof HumanPlayer ){
			List<Move> moves = ((HumanPlayer)turnPlayer).parseMoveAwnser(unparsedString);
			putMove(moves);
		}		
	}

	@Override
	public void putMove(List<Move> moves) {
		if (moves == null || moves.size() == 0) {
			this.UI.showError("Move set cannot be empty!");
			this.UI.askForMove();
		} else {
			tilesThatNeedToBeRemoved = new ArrayList<>();

			for (Move m : moves) {
				tilesThatNeedToBeRemoved.add(m.getTile());
			}

			nextDrawNeedToRemoveTiles = true;

			c.sendMessage(protocol.getInstance().clientPutMove(moves));
		}
	}
	


	@Override
	public void putTrade(String unparsedString) {
		if ( turnPlayer instanceof HumanPlayer ){
			List<Tile> trades = ((HumanPlayer)turnPlayer).parseTradeAwnser(unparsedString);
			putTrade(trades);
		}
	}

	@Override
	public void putTrade(List<Tile> tiles) {
		tilesThatNeedToBeRemoved = new ArrayList<>();
		tilesThatNeedToBeRemoved.addAll(tiles);

		nextDrawNeedToRemoveTiles = true;

		c.sendMessage(protocol.getInstance().clientTradeMove(tilesThatNeedToBeRemoved));
	}

	@Override
	public void sendChat(String msg) {
		String[] msgs = msg.split(" ");
		
		if(msgs.length < 2){
			this.UI.showError("You did not use the right format");
			this.UI.askForChatMessage();
		}
		
		String[] message = Arrays.copyOfRange(msgs, 1, msgs.length);

		StringBuilder builder = new StringBuilder();
		
		for (String s : message) {
			builder.append(s + " ");
		}

		c.sendMessage(protocol.getInstance().clientChat(msgs[0], builder.toString()));
	}

}
