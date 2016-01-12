package nl.utwente.ewi.qwirkle.client;

import java.awt.SecondaryLoop;
import java.io.IOException;
import java.net.InetAddress;

import nl.utwente.ewi.qwirkle.client.connect.Client;
import nl.utwente.ewi.qwirkle.model.player.Player;
import nl.utwente.ewi.qwirkle.protocol.protocol;
import nl.utwente.ewi.qwirkle.ui.UserInterface;
import nl.utwente.ewi.qwirkle.ui.tui.TUIView;

public class Main {

	private static final String USAGE_SERVER = "Requires 2 arguments: <host> <port>";
	private static Client client;

	public static void main(String[] args) {
	
		UserInterface UI = new TUIView();

		protocol prot = protocol.getInstance();
		//setupConnectionToServer(args);

		Player p = UI.login();		
		sendMessageToServer(prot.clientGetConnectString(p.getName(), new String[0]));

		
		int[] queues = UI.queueWithHowManyPlayers();
		// TODO make server ding
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
	
	private static void sendMessageToServer(String message){
		if(client != null && client.isAlive()){
			client.sendMessage(message);
		}else{
			System.err.println("Wanted to send message via a client that has not been started");
			System.exit(0);
		}
	}
	
	

}
