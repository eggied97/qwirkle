package nl.utwente.ewi.qwirkle.server.connect;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nl.utwente.ewi.qwirkle.model.Tile;
import nl.utwente.ewi.qwirkle.model.player.Player;
import nl.utwente.ewi.qwirkle.protocol.IProtocol;
import nl.utwente.ewi.qwirkle.protocol.protocol;
import nl.utwente.ewi.qwirkle.server.Game;
import nl.utwente.ewi.qwirkle.server.HandleCommand;

public class Server {

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println(USAGE); 
			System.exit(0);
		}

		Server server = new Server(Integer.parseInt(args[0]));
		server.run();
	}
	private final static String USAGE = "Give only the port as input";
	private int port;
	private ServerSocket serverSock;
	private List<ClientHandler> all;
	private List<ClientHandler> start;
	private List<ClientHandler> identified;
	private Map<Integer, List<ClientHandler>> queues;
	private Map<Integer, List<ClientHandler>> games;
	private HandleCommand handle = HandleCommand.getInstance(this);
	private int gameCounter = 0;
	private IProtocol.Feature[] features;

	
	public void addIdentified(List<ClientHandler> list) {
		identified.addAll(list);
	}
	
	/**
	 * 
	 * @return returns the features of the <code> Server </code>
	 */
	public IProtocol.Feature[] getFeatures() {
		return features;
	}
	
	/**
	 * 
	 * @return returns the queues of the <code> Server </code>
	 */
	public Map<Integer, List<ClientHandler>> getQueues() {
		return this.queues;
	}

	/**
	 * Create a new instance of <code> Server </code>
	 * @param port
	 */
	public Server(int port) {
		this.port = port;
		init();
	}
	
	/**
	 * Initialize the <code> Server </code>
	 */
	public void init() {
		queues = new HashMap<>();
		queues.put(2, new ArrayList<ClientHandler>());
		queues.put(3, new ArrayList<ClientHandler>());
		queues.put(4, new ArrayList<ClientHandler>());
		games = new HashMap<>();
		all = new ArrayList<>();
		start = new ArrayList<>();
		identified = new ArrayList<>();
		features = new IProtocol.Feature[1];
		features[0] = IProtocol.Feature.CHAT;
		// TODO add features when implemented
	}

	/**
	 * Starts looking for <code> Clients </code> that want to connect to the <code> Server </code>
	 */
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
	
	/**
	 * Prints the message to the standard output
	 * @param message
	 */
	public void print(String message) {
		System.out.println(message);
	}
	
	/**
	 * 
	 * @return returns the <code> List </code> with all <code> ClientHandlers </code>
	 */
	public List<ClientHandler> getAll() {
		return this.all;
	}
	
	/**
	 * Sends a message to all <code> ClientHandlers </code> of the <code> List </code>
	 * @param list
	 * @param msg
	 */
	public void broadcast(List<ClientHandler> list, String msg) {
		for(ClientHandler ch : list) {
			ch.sendMessage(msg);
		}
	}
	
	/**
	 * Removes the <code> CliendHandler </code> from the all <code> List </code>
	 * @param ch
	 */
	public void removeFromAll(ClientHandler ch) {
		all.remove(ch);
	}

	/**
	 * Removes the <code> ClientHandler </code> from his current state
	 * @param ch
	 */
	public void removeHandler(ClientHandler ch) {
		if (start.contains(ch)) {
			start.remove(ch);
			return;
		} else if (identified.contains(ch)) {
			identified.remove(ch);
			return;
		}
		// TODO add lists when created
		
		for (List<ClientHandler> l : queues.values()) {
			if (l.contains(ch)) {
				l.remove(ch);
			}
		}

	}
	
	/**
	 * If the <code> ClientHandler </code> is in a game, remove him from it
	 * @param ch
	 */
	public void removeGameHandler(ClientHandler ch) {
		for (List<ClientHandler> l : games.values()) {
			if (l.contains(ch)) {
				l.remove(ch);
			}
		}
	}
	
	/**
	 * Starts a game with the given <code> List </code> of <code> ClientHandlers </code>
	 * @param list
	 */
	public void startGame(List<ClientHandler> list) {
		List<Player> players = new ArrayList<>();
		for(ClientHandler ch : list) {
			players.add(ch.getPlayer());
		}
		Game game = new Game(list);
		broadcast(list, protocol.getInstance().serverStartGame(players));
		
		game.run();
		broadcast(list, protocol.getInstance().serverTurn(game.getPlayerTurn()));		
	}
	
	/**
	 * Checks whether one of the queues has the set amount of players required to start a <code> Game </code>
	 */
	public void checkQueues() {
		for(Entry entry: queues.entrySet()) {
			List<ClientHandler> queue = (List<ClientHandler>)entry.getValue();
			if((int)entry.getKey() == queue.size()) {
				gameCounter++;
				
				games.put(gameCounter, queues.get((int)entry.getKey()));
				
				startGame(queues.get((int)entry.getKey()));
				
				queues.get((int)entry.getKey()).clear();
				
				for(ClientHandler ch : queue) {
					removeHandler(ch);
				}
				
				return;
			}
		}
	}

	/**
	 * Analyze the input from the <code> Client </code> and act accordingly
	 * @param input
	 * @param ch
	 */
	public void getCommand(String input, ClientHandler ch) {
		String[] inputArr = input.split(" ");
		if (inputArr[0] == null) {
			ch.sendMessage(protocol.getInstance().serverError(IProtocol.Error.INVALID_COMMAND));
			return;
		}

		switch (inputArr[0]) {

			case IProtocol.CLIENT_IDENTIFY:
				if(ch.getGame() != null) {
					ch.sendMessage(protocol.getInstance().serverError(IProtocol.Error.INVALID_COMMAND));
					break;
				}
				
				handle.handleIdentifyName(inputArr,ch);
				if(handle.getWentWell()) {
					handle.setWentWell(true);
					handle.handleIdentifyFeatures(inputArr,ch);
				}
				
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
				handle.handleMovePut(inputArr, ch);
				break;
	
			case IProtocol.CLIENT_MOVE_TRADE:
				handle.handleMoveTrade(inputArr, ch);
				break;
	
			case IProtocol.CLIENT_QUEUE:
				removeHandler(ch);
				handle.handleQueue(inputArr, ch);
				if(handle.getWentWell()) {
					checkQueues();
				} else {
					removeHandler(ch);
					identified.add(ch);
				}
				handle.setWentWell(true);
				break;
			case IProtocol.CLIENT_CHAT:
				handle.handleChat(inputArr, ch);
				break;
				/*case IProtocol.CLIENT_CHALLENGE: case
				 * IProtocol.CLIENT_CHALLENGE_ACCEPT: case
				 * IProtocol.CLIENT_CHALLENGE_DECLINE: case
				 * IProtocol.CLIENT_LEADERBOARD: case IProtocol.CLIENT_LOBBY:
				 */
			default:
				ch.sendMessage(protocol.getInstance().serverError(IProtocol.Error.INVALID_COMMAND));
				break;
		}
	}

}
