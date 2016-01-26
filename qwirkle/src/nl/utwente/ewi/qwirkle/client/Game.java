package nl.utwente.ewi.qwirkle.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

import nl.utwente.ewi.qwirkle.callback.ResultCallback;
import nl.utwente.ewi.qwirkle.callback.UserInterfaceCallback;
import nl.utwente.ewi.qwirkle.client.connect.Client;
import nl.utwente.ewi.qwirkle.model.Board;
import nl.utwente.ewi.qwirkle.model.Move;
import nl.utwente.ewi.qwirkle.model.Point;
import nl.utwente.ewi.qwirkle.model.Tile;
import nl.utwente.ewi.qwirkle.model.exceptions.InvalidServerResponseException;
import nl.utwente.ewi.qwirkle.model.exceptions.PlayerNotInGameException;
import nl.utwente.ewi.qwirkle.model.exceptions.TooFewArgumentsException;
import nl.utwente.ewi.qwirkle.model.player.ComputerPlayer;
import nl.utwente.ewi.qwirkle.model.player.HumanPlayer;
import nl.utwente.ewi.qwirkle.model.player.Player;
import nl.utwente.ewi.qwirkle.model.player.strategy.DumbStrategy;
import nl.utwente.ewi.qwirkle.protocol.IProtocol;
import nl.utwente.ewi.qwirkle.protocol.IProtocol.Feature;
import nl.utwente.ewi.qwirkle.protocol.Protocol;
import nl.utwente.ewi.qwirkle.ui.UserInterface;
import nl.utwente.ewi.qwirkle.ui.gui.GUIView;
import nl.utwente.ewi.qwirkle.ui.tui.TUIView;

public class Game implements ResultCallback, UserInterfaceCallback {

	private Board board;
	private List<Player> players;
	private UserInterface UI;
	private Client c;
	private List<IProtocol.Feature> usingFeatures;
	boolean nextDrawNeedToRemoveTiles = false;
	private List<Tile> tilesThatNeedToBeRemoved;
	private Player turnPlayer;

	Map<IProtocol.Feature, Boolean> featuresEnabled;
	List<IProtocol.Feature> serverFeatures;

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
			((GUIView) UI).changeFrame();
			((GUIView) UI).setUICallback(this);
		} else {
			this.UI.askForPlayOrExchange();
		}

		c.setCallback(this); // set the callback so we receive messages from the
								// server

		this.serverFeatures = new ArrayList<>();
		this.serverFeatures.addAll(serverFeatures);

		featuresEnabled = new HashMap<>();
		checkFeatures(serverFeatures);

		this.usingFeatures = serverFeatures;
	}

	/**
	 * @return the player corresponding to the current turn
	 */
	public Player getTurnPlayer() {
		return turnPlayer;
	}

	/**
	 * @return the {@link nl.utwente.ewi.qwirkle.client.connect.Client} instance
	 *         used for the communication with the server
	 */
	public Client getClient() {
		return this.c;
	}

	/**
	 * @return the UserInterface instance that is used
	 */
	public UserInterface getUI() {
		return this.UI;
	}

	/**
	 * fills the <code> Map </code> with
	 * {@link nl.utwente.ewi.qwirkle.protocol.IProtocol.Feature} -
	 * <code> Boolean </code> pairs, for easy checking which
	 * <code>Features</code> we need to enable.
	 * 
	 * @param usingFeatures
	 *            the <code> List </code> filled with the
	 *            {@link nl.utwente.ewi.qwirkle.protocol.IProtocol.Feature} we
	 *            use
	 */
	private void checkFeatures(List<IProtocol.Feature> usingFeatures) {
		for (IProtocol.Feature f : IProtocol.Feature.values()) {
			featuresEnabled.put(f, false);
		}

		for (IProtocol.Feature f : usingFeatures) {
			featuresEnabled.put(f, true);
		}
		
		if(!featuresEnabled.get(Feature.CHAT)){
			this.UI.disableChat();
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
			Map<Player, Integer> scoreMap = new HashMap<>();

			for (Player p : players) {
				scoreMap.put(p, p.getScore());
				if(p instanceof HumanPlayer || p instanceof ComputerPlayer) {
					((GUIView)this.UI).showHand(p.getHand());
				}
			}

			((GUIView) this.UI).showBag(Math.max(0, 108 - board.getMap().size() - players.size() * 6), scoreMap, false);
			((GUIView) this.UI).updateBoard(board.getButtonBoard());
		}
	}

	@Override
	public void resultFromServer(String result) {
		try {
			if (Protocol.DEBUG) {
				System.out.println("Back from server (game)> " + result.trim());
			}

			String[] results = result.trim().split(" ");

			if (results.length == 0) {
				throw new InvalidServerResponseException();
			}

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
	
					handleDrawTile(args);
					break;
	
				case IProtocol.SERVER_GAMEEND:
					if (args.length - 1 != players.size()) {
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
	
					if (nextDrawNeedToRemoveTiles) {
						if (turnPlayer instanceof HumanPlayer || turnPlayer instanceof ComputerPlayer) {
							turnPlayer.removeTilesFromHand(tilesThatNeedToBeRemoved);
							nextDrawNeedToRemoveTiles = false;
						} else {
							// TODO is socket player -> throw error
						}
					}
					
					handleMovePut(args);
					break;
	
				case IProtocol.SERVER_MOVE_TRADE:
					if (args.length < 1) {
						throw new TooFewArgumentsException(args.length);
					}
					
					if (nextDrawNeedToRemoveTiles) {
						if (turnPlayer instanceof HumanPlayer || turnPlayer instanceof ComputerPlayer) {
							turnPlayer.removeTilesFromHand(tilesThatNeedToBeRemoved);
							nextDrawNeedToRemoveTiles = false;
						} else {
							// TODO is socket player -> throw error
						}
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
					if (args.length < 1) {
						throw new TooFewArgumentsException(args.length);
					}
	
					switch (IProtocol.Error.valueOf(args[0])) {
						case MOVE_INVALID:
							this.UI.showError("The move you made was invalid.");
							handleProblemWithMove();
							break;
		
						case MOVE_TILES_UNOWNED:
							this.UI.showError("The move you made was using tiles that you did not own.");
							handleTurn(true);
							break;
		
						case TRADE_FIRST_TURN:
							this.UI.showError("You wanted to trade on your first turn, you cant do this.");
							handleTurn(true);
		
						case INVALID_CHANNEL:
							this.UI.showError(
									"You wanted to send a message to a unknow channel. \n Usernames are uppercase specific , or use `global`.");
							handleTurn(true);
							break;
							
						case INVALID_PARAMETER:
							this.UI.showError(
									"you have send an invalid parameter, try again.");
							handleTurn(true);
							break;
		
						case INVALID_COMMAND:
							this.UI.showError(
									"you have send an invalid command, try again.");
							handleTurn(true);
							break;
							
						default:
							break;
					}

				break;
			}
		} catch (TooFewArgumentsException | InvalidServerResponseException | PlayerNotInGameException e) {
			this.UI.showError("Something went bad with the protocol message : " + e.getMessage());
		}
	}

	/**
	 * Used when there was a invalid move.
	 */
	private void handleProblemWithMove() {
		if (this.UI instanceof GUIView) {
			((GUIView) this.UI).handleProblemWithMove();
		}

		handleTurn(true);
	}

	/**
	 * handles an incomming chat message.
	 * 
	 * @param args
	 */
	private void handleIncommingChatMessage(String[] args) {

		Boolean isGlobal = args[0].equals("global");
		String sender = args[1];

		// only handle this if we didnt send this
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
			if (this.UI instanceof TUIView) {
				this.UI.printMessage("Turn changed, its " + turnPlayer.getName() + " turn now");
			} else {
				this.UI.printMessage(turnPlayer.getName() + "'s turn");
			}
		}
	}

	/**
	 * Handles the pc turn, for the timed excecution see
	 * http://stackoverflow.com/questions/20500003/setting-a-maximum-execution-
	 * time-for-a-method-thread .
	 */
	private void handleTurnPcPlayer() {
		ExecutorService executor = Executors.newSingleThreadExecutor();

		Future<?> future = executor.submit(new Runnable() {
			@Override
			public void run() {
				doPcTurn();
			}
		});

		executor.shutdown(); // close it so nothing can join it

		try {
			// set waiting time
			future.get(((ComputerPlayer) turnPlayer).getTime(), TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			System.out.println("job was interrupted");
		} catch (ExecutionException e) {
			System.out.println("caught exception: " + e.getCause());
		} catch (TimeoutException e) {
			future.cancel(true); // interrupt it, because it took too long

			// Player took too long -> we trade the first tile in our hand as
		}

		// TODO check how this works out
		// wait all unfinished tasks for 2 sec
		try {
			if (!executor.awaitTermination(2, TimeUnit.SECONDS)) {
				// force them to quit by interrupting
				executor.shutdownNow();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * determines the move of the Computer player.
	 */
	private void doPcTurn() {
		List<Move> moves = turnPlayer.determinePutMove(board);

		if (moves.size() == 0) {
			List<Tile> tiles = turnPlayer.determineTradeMove();

			tilesThatNeedToBeRemoved = new ArrayList<>();
			tilesThatNeedToBeRemoved.addAll(tiles);

			nextDrawNeedToRemoveTiles = true;

			c.sendMessage(Protocol.getInstance().clientTradeMove(tilesThatNeedToBeRemoved));
		} else {
			tilesThatNeedToBeRemoved = new ArrayList<>();

			for (Move m : moves) {
				tilesThatNeedToBeRemoved.add(m.getTile());
			}

			nextDrawNeedToRemoveTiles = true;

			c.sendMessage(Protocol.getInstance().clientPutMove(moves));
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
	private void handleGameEnd(String[] args) throws TooFewArgumentsException, PlayerNotInGameException {
		Map<Player, Integer> scoreMap = new HashMap<>();

		String errorOrWin = args[0];

		String[] newArgs = Arrays.copyOfRange(args, 1, args.length);

		for (String a : newArgs) {
			String[] scoreNaam = a.split(",");

			if (scoreNaam.length != 2) {
				throw new TooFewArgumentsException(scoreNaam.length);
			}

			Player p = getPlayerByName(scoreNaam[0]);

			if (p != null) {
				scoreMap.put(p, Integer.parseInt(scoreNaam[1]));
			} else {
				throw new PlayerNotInGameException(scoreNaam[0]);
			}
		}

		// TODO order the map

		this.UI.printMessage("Win by " + (errorOrWin.equals("WIN") ? "win" : "error") + " :");
		this.UI.showScore(scoreMap, true);

		playing = false;

		Player me = null;

		for (Player p : players) {
			if (p instanceof HumanPlayer || p instanceof ComputerPlayer) {
				me = p;
			}
		}

		new QwirkleClient(me, c, this.UI, this.serverFeatures);
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

	/*
	 * ===================
	 * Callback methods
	 * ===================
	 * */
	
	@Override
	public void login(Player p) {} // not called
	
	@Override
	public void setupServer(String serverInformation) {}// not used

	@Override
	public void queue(int[] queue) {} // not called
	

	@Override
	public void setAITime(int time) {} // not used

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
				if (turnPlayer instanceof HumanPlayer) {
					this.UI.askForTrade();
				} else {
					this.UI.showError("Wait for your turn");
				}
	
				break;
	
			case "s":
				Map<Player, Integer> scoreMap = new HashMap<>();
	
				for (Player p : players) {
					scoreMap.put(p, p.getScore());
				}
	
				// TODO sort map
	
				this.UI.showScore(scoreMap, false);
				break;
	
			case "c":
				this.UI.askForChatMessage();
				break;
	
			case "q":
				quit();
				break;
	
			default:
				this.UI.showError("Wrong argument.");
				handleTurn(true);
				break;
		}
	}

	@Override
	public void putMove(String unparsedString) {
		if (turnPlayer instanceof HumanPlayer) {
			List<Move> moves = ((HumanPlayer) turnPlayer).parseMoveAwnser(unparsedString);
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

			c.sendMessage(Protocol.getInstance().clientPutMove(moves));
		}
	}

	@Override
	public void putTrade(String unparsedString) {
		if (turnPlayer instanceof HumanPlayer) {
			List<Tile> trades = ((HumanPlayer) turnPlayer).parseTradeAwnser(unparsedString);
			putTrade(trades);
		}
	}

	@Override
	public void putTrade(List<Tile> tiles) {
		tilesThatNeedToBeRemoved = new ArrayList<>();
		tilesThatNeedToBeRemoved.addAll(tiles);

		nextDrawNeedToRemoveTiles = true;

		c.sendMessage(Protocol.getInstance().clientTradeMove(tilesThatNeedToBeRemoved));
	}

	@Override
	public void sendChat(String msg) {

		String[] msgs = msg.split(" ");

		if (msgs.length < 2) {
			this.UI.showError("You did not use the right format");
			this.UI.askForChatMessage();
		}

		String[] message = Arrays.copyOfRange(msgs, 1, msgs.length);

		StringBuilder builder = new StringBuilder();

		for (String s : message) {
			builder.append(s + " ");
		}

		c.sendMessage(Protocol.getInstance().clientChat(msgs[0], builder.toString()));
	}

	

	@Override
	public void printHint() {
		List<Move> hint = new DumbStrategy().determineMove(board, turnPlayer.getHand());

		if(this.UI instanceof TUIView) {
			this.UI.printMessage(hint.get(0).toHumanString());
		} else {
			((GUIView)this.UI).printHint(hint.get(0));
		}
	}

	@Override
	public void quit() {
		// send message and go back to the main menu
		c.sendMessage(Protocol.getInstance().clientQuit());
		playing = false;

		Player me = null;

		for (Player p : players) {
			if (p instanceof HumanPlayer || p instanceof ComputerPlayer) {
				me = p;
			}
		}

		new QwirkleClient(me, c, this.UI, this.serverFeatures);

	}


}
