package nl.utwente.ewi.qwirkle.client;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.utwente.ewi.qwirkle.util;
import nl.utwente.ewi.qwirkle.callback.ResultCallback;
import nl.utwente.ewi.qwirkle.callback.UserInterfaceCallback;
import nl.utwente.ewi.qwirkle.client.connect.Client;
import nl.utwente.ewi.qwirkle.model.Move;
import nl.utwente.ewi.qwirkle.model.Tile;
import nl.utwente.ewi.qwirkle.model.exceptions.InvalidServerResponseException;
import nl.utwente.ewi.qwirkle.model.exceptions.TooFewArgumentsException;
import nl.utwente.ewi.qwirkle.model.exceptions.TooFewPlayersException;
import nl.utwente.ewi.qwirkle.model.exceptions.TooManyPlayersException;
import nl.utwente.ewi.qwirkle.model.player.ComputerPlayer;
import nl.utwente.ewi.qwirkle.model.player.HumanPlayer;
import nl.utwente.ewi.qwirkle.model.player.Player;
import nl.utwente.ewi.qwirkle.model.player.SocketPlayer;
import nl.utwente.ewi.qwirkle.protocol.IProtocol;
import nl.utwente.ewi.qwirkle.protocol.Protocol;
import nl.utwente.ewi.qwirkle.protocol.IProtocol.Feature;
import nl.utwente.ewi.qwirkle.ui.UserInterface;
import nl.utwente.ewi.qwirkle.ui.gui.GUIView;
import nl.utwente.ewi.qwirkle.ui.tui.TUIView;

public class QwirkleClient implements ResultCallback, UserInterfaceCallback {

	private final String USAGE_SERVER = "Requires 2 arguments: <host> <port>";
	private Client server;

	private UserInterface UI;
	private Protocol prot;

	private Player me;

	private IProtocol.Feature[] implementedFeatures = { Feature.CHAT };
	private List<IProtocol.Feature> usingFeatures;

	public static void main(String[] args) {
		QwirkleClient qc = new QwirkleClient(args);
	}

	/**
	 * Constructor if you open this program for the first time.
	 * 
	 * @param args
	 *            of the terminal
	 */
	public QwirkleClient(String[] args) {
		// set up some variables
		this.usingFeatures = new ArrayList<>();
		this.UI = new GUIView();
		this.prot = Protocol.getInstance();

		this.UI.setCallback(this); // set the callback for UI changes
		if (this.UI instanceof GUIView) {
			((GUIView) this.UI).setup(this);
		}

		this.UI.askForServerInformation();
	}
	
	/**
	 * Constructor that is called from the main menu.
	 * 
	 * @param port
	 */
	public QwirkleClient(int port){
		this.usingFeatures = new ArrayList<>();
		this.UI = new GUIView();
		this.prot = Protocol.getInstance();

		this.UI.setCallback(this); // set the callback for UI changes
		if (this.UI instanceof GUIView) {
			((GUIView) this.UI).setup(this);
		}

		setupServer("localhost@"+port);
		
	}

	/**
	 * Constructor after a game.
	 * 
	 * @param me
	 *            you, the player
	 * @param server
	 *            the {@link nl.utwente.ewi.qwirkle.client.connect.Client}
	 *            instance of the server
	 * @param ui
	 *            the {@link nl.utwente.ewi.qwirkle.ui.UserInterface} instance
	 * @param usingFeatures
	 *            a <code>List</code> of
	 *            {@link nl.utwente.ewi.qwirkle.protocol.IProtocol.Feature} that
	 *            are enabeld>
	 */
	public QwirkleClient(Player me, Client server, UserInterface ui, List<IProtocol.Feature> usingFeatures) {
		this.me = me;
		this.server = server;
		this.server.setCallback(this);
		this.UI = ui;

		this.usingFeatures = usingFeatures;

		this.UI.setCallback(this);

		prot = Protocol.getInstance();

		if (this.UI instanceof GUIView) {
			((GUIView) this.UI).changeFrame();
		}

		enterQueue();
	}

	/**
	 * Asks the {@link nl.utwente.ewi.qwirkle.ui.UserInterface} instance to
	 * authenticate the player.
	 */
	private void authenticateUser() {
		this.UI.askForLogin();
	}

	/**
	 * asks the {@link nl.utwente.ewi.qwirkle.model.player.Player} to join a
	 * queue.
	 */
	private void enterQueue() {
		this.UI.askQueueWithHowManyPlayers();
	}

	/**
	 * sends a given mesage to the server instance. (it exitsts when the server
	 * is not set, or it is offline)
	 * 
	 * @param message
	 *            the message that needs to be sent
	 */
	private void sendMessageToServer(String message) {
		if (this.server != null && this.server.isAlive()) {
			this.server.sendMessage(message);
		} else {
			this.UI.showError("Wanted to send message via a server that has not been started");
			System.exit(0);
		}
	}

	@Override
	public void resultFromServer(String result) {
		System.out.println("Back from server (main)> " + result.trim());

		try{
			String[] results = result.trim().split(" ");
	
			if (results.length == 0) {
				throw new InvalidServerResponseException();
			}
	
			String command = results[0];
			String[] args = Arrays.copyOfRange(results, 1, results.length);
	
			switch (command) {
				case IProtocol.SERVER_IDENTIFY:
					handleServerIdentify(args);
					break;
		
				case IProtocol.SERVER_GAMESTART:
					try {
						handleGameStart(args);
					} catch (TooManyPlayersException | TooFewPlayersException e) {
						e.printStackTrace();
					}
					break;
		
				case IProtocol.SERVER_ERROR:
					if (args.length < 1) {
						throw new TooFewArgumentsException(args.length);
					}
		
					switch (IProtocol.Error.valueOf(args[0])) {
						case NAME_USED:
							UI.showError("Name is already in use, please use another one...");
							authenticateUser();
							break;
			
						case NAME_INVALID:
							UI.showError("Name is invalid, please use another one...");
							authenticateUser();
							break;
			
						case QUEUE_INVALID:
							UI.showError("The queue you wanted to enter is invalid, please choose another one...");
							enterQueue();
							break;
		
					default:
						break;
					}
					break;
		
				default:
					break;
			}
		}catch(InvalidServerResponseException | TooFewArgumentsException e){
			this.UI.showError("Something went bad with the protocol message : " + e.getMessage());
		}
	}

	/**
	 * handles the input from the server when the server wants to start a game.
	 * 
	 * @param args
	 *            of the server
	 * @throws TooManyPlayersException
	 *             when there are too many players (>4 players total)
	 * @throws TooFewPlayersException
	 *             when there are too few players.
	 */
	private void handleGameStart(String[] args) throws TooManyPlayersException, TooFewPlayersException {
		if (args.length > 4) {
			throw new TooManyPlayersException(args.length + 1);
		} else if (args.length == 0) {
			throw new TooFewPlayersException(args.length + 1);
		}

		List<Player> playersInGame = new ArrayList<>();

		playersInGame.add(me);

		for (String name : args) {
			if (!name.equals(me.getName())) {
				Player p = new SocketPlayer(name);
				playersInGame.add(p);
			}
		}

		me.newGame(); // so we start clear on a possible new match

		Game g = new Game(UI, playersInGame, server, usingFeatures);
		server.setCallback(g);
		UI.setCallback(g);

		if (me instanceof HumanPlayer) {
			((HumanPlayer) me).setGame(g);
		}
	}

	/**
	 * handles the input from the server when server wants to authenticate.
	 * 
	 * @param args
	 *            of the server
	 */
	private void handleServerIdentify(String[] args) {
		String s = "";
		
		for(String g : args){
			s += g;
		}
		
		String[] wots = s.split(",");
		
		if (args.length >= 1) {
			for (String r : wots) {
				if (util.FeatureArrayContains(implementedFeatures, IProtocol.Feature.valueOf(r))) {
					usingFeatures.add(IProtocol.Feature.valueOf(r));
				}
			}
		}

		// now go and ask for the queue
		enterQueue();
	}

	/*===========
	 * Callback methods
	 * ===========
	 * */
	
	@Override
	public void setupServer(String serverInformation) {
		String[] parts = serverInformation.split("@");

		if (parts.length != 2) {
			this.UI.showError("Wrong format.");
			this.UI.askForServerInformation();
		} else {
			InetAddress host = null;
			int port = 0;

			try {
				host = InetAddress.getByName(parts[0]);
				port = Integer.parseInt(parts[1]);

				server = new Client(host, port);
				server.start();

				this.server.setCallback(this); // set the callback for incomming
												// server messages

				authenticateUser();

			} catch (NumberFormatException | IOException e) {
				this.UI.showError("Server doesnt exist, or you entered the wrong port");
				this.UI.askForServerInformation();
			}
		}
	}

	@Override
	public void login(Player p) {
		me = p;
		if (this.UI instanceof GUIView) {
			((GUIView) this.UI).setPlayer(p);
		}

		if (me instanceof ComputerPlayer) {
			this.UI.askForAITime();
		} else {
			sendMessageToServer(prot.clientGetConnectString(me.getName(), implementedFeatures));
		}
	}

	@Override
	public void setAITime(int time) {
		if (me != null && me instanceof ComputerPlayer) {
			((ComputerPlayer) me).setTime(time);
		}

		sendMessageToServer(prot.clientGetConnectString(me.getName(), implementedFeatures));
	}

	@Override
	public void queue(int[] queue) {
		sendMessageToServer(prot.clientQueue(queue));
	}

	@Override
	public void determinedAction(String action) {} // not used

	@Override
	public void putMove(String unparsedString) {} // not used

	@Override
	public void putMove(List<Move> moves) {} // not used

	@Override
	public void putTrade(String unparsedString) {} // not used

	@Override
	public void putTrade(List<Tile> tiles) {} // not used

	@Override
	public void sendChat(String msg) {} // not used

	@Override
	public void printHint() {} // not used

	@Override
	public void quit() {} // not used

}
