package nl.utwente.ewi.qwirkle.server.connect;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nl.utwente.ewi.qwirkle.protocol.IProtocol;
import nl.utwente.ewi.qwirkle.server.HandleCommand;

public class Server {

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Give only the port as input");
			System.exit(0);
		}

		Server server = new Server(Integer.parseInt(args[0]));
		server.run();
	}

	private int port;
	private ServerSocket serverSock;
	private List<ClientHandler> start;
	private List<ClientHandler> identified;
	private Map<Integer, List<ClientHandler>> queues;
	private Map<Integer, List<ClientHandler>> games;
	private HandleCommand handle = HandleCommand.getInstance();
	
	
	public Server(int port) {
		this.port = port;
		queues.put(2, new ArrayList<ClientHandler>());
		queues.put(3, new ArrayList<ClientHandler>());
		queues.put(4, new ArrayList<ClientHandler>());
	}

	public void run() {
		try {
			serverSock = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}

		while (true) {
			try {
				Socket client = serverSock.accept();
				System.out.println("Client connected!");
				ClientHandler handler = new ClientHandler(this, client);
				handler.start();
				start.add(handler);
				sendIdentifier();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void sendIdentifier() {
		for (ClientHandler ch : start) {
			ch.sendMessage("Identify yourself: CONNECT <name> [<feature>]");
		}
	}

	public void removeHandler(ClientHandler ch) {
		if (start.contains(ch)) {
			start.remove(ch);
			return;
		} else if (identified.contains(ch)) {
			identified.remove(ch);
			return;
		}
		// TODO add lists when created
		for (List<ClientHandler> l : games.values()) {
			if (l.contains(ch)) {
				l.remove(ch);
			}
		}
		for (List<ClientHandler> l : queues.values()) {
			if (l.contains(ch)) {
				l.remove(ch);
			}
		}

	}
	// TODO error handling
	public void getCommand(String str, ClientHandler ch) {
		String[] s = str.split(" ");
		if (s[0] == null) {
			ch.sendMessage(IProtocol.Error.COMMAND_NOT_FOUND.toString());
			return;
		}
		
		switch (s[0]) {
		
		case IProtocol.CLIENT_IDENTIFY:
			String name = handle.handleIdentifyName(s);
			if(name.equals(null)) {
				ch.sendMessage(IProtocol.Error.NAME_INVALID.toString());
				return;
			}
			ch.setClientName(name);
			List<IProtocol.Feature> features = handle.handleIdentifyFeatures(s);
			ch.setFeatures(features);
			removeHandler(ch);
			identified.add(ch);
			break;
		case IProtocol.CLIENT_QUIT:
		case IProtocol.CLIENT_MOVE_PUT:
		case IProtocol.CLIENT_MOVE_TRADE:
		case IProtocol.CLIENT_QUEUE:

			/*
			 * case IProtocol.CLIENT_CHAT: 
			 * case IProtocol.CLIENT_CHALLENGE: 
			 * case IProtocol.CLIENT_CHALLENGE_ACCEPT: 
			 * case IProtocol.CLIENT_CHALLENGE_DECLINE: 
			 * case IProtocol.CLIENT_LEADERBOARD: 
			 * case IProtocol.CLIENT_LOBBY:
			 */
		default:
			ch.sendMessage(IProtocol.Error.COMMAND_NOT_FOUND.toString());
			break;
		}
	}

}
