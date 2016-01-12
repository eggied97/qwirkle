package nl.utwente.ewi.qwirkle.client;

import java.awt.SecondaryLoop;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.utwente.ewi.qwirkle.util;
import nl.utwente.ewi.qwirkle.client.connect.Client;
import nl.utwente.ewi.qwirkle.client.connect.resultCallback;
import nl.utwente.ewi.qwirkle.model.player.Player;
import nl.utwente.ewi.qwirkle.protocol.IProtocol;
import nl.utwente.ewi.qwirkle.protocol.protocol;
import nl.utwente.ewi.qwirkle.ui.UserInterface;
import nl.utwente.ewi.qwirkle.ui.tui.TUIView;

public class Main implements resultCallback {

	private static final String USAGE_SERVER = "Requires 2 arguments: <host> <port>";
	private static Client client;

	private static UserInterface UI;
	private static protocol prot;

	private static IProtocol.Feature[] implementedFeatures = new IProtocol.Feature[] {};
	private static List<IProtocol.Feature> usingFeatures;

	public static void main(String[] args) {
		usingFeatures = new ArrayList<>();
		UI = new TUIView();

		prot = protocol.getInstance();
		// setupConnectionToServer(args);

		authenticateUser();
	}

	private static void authenticateUser() {
		Player p = UI.login();
		sendMessageToServer(prot.clientGetConnectString(p.getName(), implementedFeatures));
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

		client = new Client(host, port);
		client.start();
	}

	private static void sendMessageToServer(String message) {
		if (client != null && client.isAlive()) {
			client.sendMessage(message);
		} else {
			System.err.println("Wanted to send message via a client that has not been started");
			System.exit(0);
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
		case IProtocol.SERVER_IDENITFY:
			handleServerIdentify(args);
			break;
		
		case IProtocol.SERVER_GAMESTART:
			handleGameStart(args);
			break;

		case IProtocol.SERVER_ERROR:
			break;

		default:
			break;
		}
	}
	
	private void handleGameStart(String[] args){
		
	}

	private void handleServerIdentify(String[] args) {
		if (args.length > 1) {
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