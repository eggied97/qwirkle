package nl.utwente.ewi.qwirkle.client;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.utwente.ewi.qwirkle.util;
import nl.utwente.ewi.qwirkle.client.connect.Client;
import nl.utwente.ewi.qwirkle.client.connect.ResultCallback;
import nl.utwente.ewi.qwirkle.model.exceptions.TooFewPlayersException;
import nl.utwente.ewi.qwirkle.model.exceptions.TooManyPlayersException;
import nl.utwente.ewi.qwirkle.model.player.HumanPlayer;
import nl.utwente.ewi.qwirkle.model.player.Player;
import nl.utwente.ewi.qwirkle.model.player.SocketPlayer;
import nl.utwente.ewi.qwirkle.protocol.IProtocol;
import nl.utwente.ewi.qwirkle.protocol.protocol;
import nl.utwente.ewi.qwirkle.protocol.IProtocol.Feature;
import nl.utwente.ewi.qwirkle.ui.UserInterface;
import nl.utwente.ewi.qwirkle.ui.gui.GUIView;
import nl.utwente.ewi.qwirkle.ui.tui.TUIView;

public class Main implements ResultCallback {

	private static final String USAGE_SERVER = "Requires 2 arguments: <host> <port>";
	private static Client server;

	private static UserInterface UI;
	private static protocol prot;

	private static Player me;

	private static IProtocol.Feature[] implementedFeatures = {Feature.CHAT};
	private static List<IProtocol.Feature> usingFeatures;

	public static void main(String[] args) {
		Main m = new Main(args);
	}

	//In the start
	public Main(String[] args) {
		usingFeatures = new ArrayList<>();
		UI = new TUIView();

		prot = protocol.getInstance();
		setupConnectionToServer(args);

		server.setCallback(this);

		authenticateUser();
	}
	
	//after a match
	public Main(Player me, Client server,UserInterface ui){
		this.me = me;
		this.server = server;
		this.server.setCallback(this);
		this.UI = ui;
		
		prot = protocol.getInstance();
		enterQueue();
	}

	private static void authenticateUser() {
		me = UI.login();
		sendMessageToServer(prot.clientGetConnectString(me.getName(), implementedFeatures));
	}

	private void enterQueue() {
		int[] queues = UI.queueWithHowManyPlayers();
		sendMessageToServer(prot.clientQueue(queues));
	}

	private static void setupConnectionToServer(String[] args) {
		if (args.length != 2) {
			System.out.println(USAGE_SERVER);
			System.exit(0);
		}

		InetAddress host = null;
		int port = 0;

		try {
			host = InetAddress.getByName(args[0]);
			port = Integer.parseInt(args[1]);

		} catch (NumberFormatException | IOException e) {
			e.printStackTrace(System.out);
			System.exit(0);

			/*
			 * System.out.println("No valid portnumber"); System.exit(0);
			 * System.out.println("No host known by that name"); System.exit(0);
			 */

		}

		server = new Client(host, port);
		server.start();
	}

	private static void sendMessageToServer(String message) {
		if (server != null && server.isAlive()) {
			server.sendMessage(message);
		} else {
			UI.showError("Wanted to send message via a server that has not been started");
			System.exit(0);
		}
	}

	@Override
	public void resultFromServer(String result) {
		System.out.println("Back from server (main)> " + result.trim());

		String[] results = result.trim().split(" ");

		if (results.length == 0) {
			// TODO throw error
		}

		String command = results[0];
		String[] args = Arrays.copyOfRange(results, 1, results.length);

		switch (command) {
			case IProtocol.SERVER_IDENITFY:
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
					// TODO throw error
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
	}

	private void handleGameStart(String[] args) throws TooManyPlayersException, TooFewPlayersException {
		if (args.length > 4) {
			throw new TooManyPlayersException(args.length + 1);
		} else if (args.length == 0) {
			throw new TooFewPlayersException(args.length + 1);
		}

		List<Player> playersInGame = new ArrayList<>();

		playersInGame.add(me);

		for (String name : args) {
			Player p = new SocketPlayer(name);
			playersInGame.add(p);
		}

		Game g = new Game(UI, playersInGame, server, usingFeatures);
		server.setCallback(g);
		
		if (me instanceof HumanPlayer) {
			((HumanPlayer) me).setGame(g);
		}
	}

	private void handleServerIdentify(String[] args) {
		if (args.length >= 1) {
			for (String r : args) {
				if (util.FeatureArrayContains(implementedFeatures, IProtocol.Feature.valueOf(r))) {
					usingFeatures.add(IProtocol.Feature.valueOf(r));
				}
			}
		}

		// now go and ask for the queue
		enterQueue();
	}

}
