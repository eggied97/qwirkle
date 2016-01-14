package nl.utwente.ewi.qwirkle.server.connect;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nl.utwente.ewi.qwirkle.model.player.Player;
import nl.utwente.ewi.qwirkle.protocol.IProtocol;
import nl.utwente.ewi.qwirkle.protocol.protocol;
import nl.utwente.ewi.qwirkle.server.Game;
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
	private List<ClientHandler> all;
	private List<ClientHandler> start;
	private List<ClientHandler> identified;
	private Map<Integer, List<ClientHandler>> queues;
	private Map<Integer, List<ClientHandler>> games;
	private HandleCommand handle = HandleCommand.getInstance(this);
	private int gameCounter = 0;

	public Map<Integer, List<ClientHandler>> getQueues() {
		return this.queues;
	}

	public Server(int port) {
		this.port = port;
		init();
		queues.put(2, new ArrayList<ClientHandler>());
		queues.put(3, new ArrayList<ClientHandler>());
		queues.put(4, new ArrayList<ClientHandler>());
	}
	
	public void init() {
		queues = new HashMap<>();
		games = new HashMap<>();
		all = new ArrayList<>();
		start = new ArrayList<>();
		identified = new ArrayList<>();
		
		
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
				all.add(handler);
				start.add(handler);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public List<ClientHandler> getAll() {
		return this.all;
	}
	
	public void broadcast(List<ClientHandler> list, String msg) {
		for(ClientHandler ch : list) {
			ch.sendMessage(msg);
		}
	}
	
	public void removeFromAll(ClientHandler ch) {
		all.remove(ch);
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
	
	public void startGame(List<ClientHandler> list) {
		List<Player> players = new ArrayList<>();
		for(ClientHandler ch : list) {
			players.add(ch.getPlayer());
		}
		Game game = new Game(list);
		broadcast(list, protocol.getInstance().serverStartGame(players));
		for(ClientHandler ch : list) {
			ch.setGame(game);
		}
		
		game.start();
		
	}
	
	public void checkQueues() {
		for(Entry entry: queues.entrySet()) {
			List<ClientHandler> queue = (List<ClientHandler>)entry.getValue();
			if((int)entry.getKey() == queue.size()) {
				gameCounter++;
				for(ClientHandler ch : queue) {
					removeHandler(ch);
				}
				games.put(gameCounter, queue);
				startGame(queue);
				queues.get((int)entry.getKey()).clear();
				return;
			}
		}
	}

	public void getCommand(String str, ClientHandler ch) {
		String[] s = str.split(" ");
		if (s[0] == null) {
			ch.sendMessage(protocol.SERVER_ERROR + IProtocol.Error.INVALID_COMMAND.toString());
			return;
		}

		switch (s[0]) {

		case IProtocol.CLIENT_IDENTIFY:
			if(ch.getGame() != null) {
				ch.sendMessage(protocol.SERVER_ERROR + IProtocol.Error.INVALID_COMMAND.toString());
				break;
			}
			handle.handleIdentifyName(s,ch);
			handle.handleIdentifyFeatures(s,ch);
			if(handle.getWentWell()) {
				removeHandler(ch);
				identified.add(ch);	
			}
			handle.setWentWell(true);
			break;

		case IProtocol.CLIENT_QUIT:
			ch.shutDown();
			break;

		case IProtocol.CLIENT_MOVE_PUT:
			handle.handleMovePut(s, ch);
			break;

		case IProtocol.CLIENT_MOVE_TRADE:
			handle.handleMoveTrade(s, ch);
			break;

		case IProtocol.CLIENT_QUEUE:
			removeHandler(ch);
			handle.handleQueue(s, ch);
			checkQueues();
			break;
			/*
			 * case IProtocol.CLIENT_CHAT: case IProtocol.CLIENT_CHALLENGE: case
			 * IProtocol.CLIENT_CHALLENGE_ACCEPT: case
			 * IProtocol.CLIENT_CHALLENGE_DECLINE: case
			 * IProtocol.CLIENT_LEADERBOARD: case IProtocol.CLIENT_LOBBY:
			 */
		default:
			ch.sendMessage(protocol.SERVER_ERROR + IProtocol.Error.INVALID_COMMAND.toString());
			break;
		}
	}

}
